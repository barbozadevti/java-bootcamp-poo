#!/usr/bin/env bash
# Compila o código e roda os testes (sem dependências externas) e o Main.
set -euo pipefail
cd "$(dirname "$0")/.."
rm -rf out && mkdir -p out
javac -encoding UTF-8 -d out src/*.java tests/Testes.java
java -Dstdout.encoding=UTF-8 -cp out Testes

echo
echo "--- Main ---"
saida=$(java -Dstdout.encoding=UTF-8 -cp out Main | tr -d '\r')
echo "$saida" | head -3
grep -qF "Concluiu: Abstraindo um bootcamp | XP: 210" <<< "$saida" && echo "ok    Main conclui a trilha da Camila com 210 XP"
grep -qF "não tem mais vagas" <<< "$saida" && echo "ok    Main mostra a inscrição recusada por falta de vagas"
