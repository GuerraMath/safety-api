package io.github.guerramath.safety_api.security;

import io.github.guerramath.safety_api.service.JwtService;
import io.github.guerramath.safety_api.repository.UserRepository;
import io.github.guerramath.safety_api.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filtro para validacao de JWT em requisicoes.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    // Cache simples para evitar queries repetidas ao banco
    private final ConcurrentHashMap<Long, CachedUser> userCache = new ConcurrentHashMap<>();
    private static final long CACHE_TTL_MS = TimeUnit.MINUTES.toMillis(5);

    private static class CachedUser {
        final User user;
        final long timestamp;

        CachedUser(User user) {
            this.user = user;
            this.timestamp = System.currentTimeMillis();
        }

        boolean isExpired() {
            return System.currentTimeMillis() - timestamp > CACHE_TTL_MS;
        }
    }

    public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String jwt = extractJwtFromRequest(request);

        // Retorna cedo se não há token - evita processamento desnecessário
        if (!StringUtils.hasText(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (jwtService.isTokenValid(jwt)) {
                String userId = jwtService.extractUserId(jwt);
                Long userIdLong = Long.parseLong(userId);

                User user = getUserFromCacheOrDb(userIdLong);

                if (user != null) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    null);
                    authentication.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (NumberFormatException e) {
            logger.debug("ID de usuario invalido no token JWT");
        } catch (Exception e) {
            logger.error("Falha ao processar JWT token: " + e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }

    private User getUserFromCacheOrDb(Long userId) {
        CachedUser cached = userCache.get(userId);

        if (cached != null && !cached.isExpired()) {
            return cached.user;
        }

        // Remove entrada expirada se existir
        if (cached != null) {
            userCache.remove(userId);
        }

        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            userCache.put(userId, new CachedUser(user));
        }

        return user;
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
