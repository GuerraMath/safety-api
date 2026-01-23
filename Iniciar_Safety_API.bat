@echo off
title SMS Safety API - Sistema de Inicializacao
color 0B
echo ======================================================
echo    INICIANDO SISTEMA DE SEGURANCA OPERACIONAL
echo ======================================================
echo.

:: Comando "mágico" que identifica a pasta onde o .bat está salvo
cd /d "%~dp0"

echo [OK] Diretorio identificado: %cd%
echo [..] Verificando motor Java e banco de dados...
echo.

:: Executa o JAR dentro da pasta target
java -jar target/safety-api-0.0.1-SNAPSHOT.jar

if %errorlevel% neq 0 (
    echo.
    echo [ERRO] Falha na decolagem. Verifique se:
    echo 1. O arquivo .jar existe na pasta 'target'
    echo 2. O banco PostgreSQL esta rodando
    echo 3. O Java 17+ esta instalado
)

pause