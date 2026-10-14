#!/usr/bin/env bash
# Sobe o HelpDesk sem Eclipse (Mac/Linux). Precisa de: Java 21+, internet na 1a vez e MySQL ligado.
# Baixa Maven e Tomcat 11 para a pasta .ferramentas/ (dentro do projeto) e usa só eles.
set -e
cd "$(dirname "$0")"
RAIZ="$(pwd)"
FERR="$RAIZ/.ferramentas"
MAVEN_VER=3.9.9
TOMCAT_VER=11.0.4
mkdir -p "$FERR"

# ---- 1. Java 21+ ----
if ! command -v java >/dev/null 2>&1; then
  echo "Java nao encontrado. Instale o JDK 21 e tente de novo."; exit 1
fi
JV=$(java -version 2>&1 | head -n1 | sed -E 's/.*version "([0-9]+).*/\1/')
if [ "${JV:-0}" -lt 21 ]; then
  echo "Java $JV encontrado, mas o projeto precisa do Java 21 ou mais novo."; exit 1
fi
if [ -z "$JAVA_HOME" ]; then
  JAVA_HOME="$(dirname "$(dirname "$(readlink -f "$(command -v java)")")")"
  export JAVA_HOME
fi

# ---- 2. Maven ----
if command -v mvn >/dev/null 2>&1; then
  MVN=mvn
else
  if [ ! -x "$FERR/apache-maven-$MAVEN_VER/bin/mvn" ]; then
    echo "Baixando Maven $MAVEN_VER..."
    curl -fL "https://archive.apache.org/dist/maven/maven-3/$MAVEN_VER/binaries/apache-maven-$MAVEN_VER-bin.tar.gz" -o "$FERR/maven.tar.gz"
    tar -xzf "$FERR/maven.tar.gz" -C "$FERR" && rm "$FERR/maven.tar.gz"
  fi
  MVN="$FERR/apache-maven-$MAVEN_VER/bin/mvn"
fi

# ---- 3. Tomcat 11 ----
if [ -n "$CATALINA_HOME" ] && [ -x "$CATALINA_HOME/bin/catalina.sh" ]; then
  TOMCAT="$CATALINA_HOME"
else
  TOMCAT="$FERR/apache-tomcat-$TOMCAT_VER"
  if [ ! -x "$TOMCAT/bin/catalina.sh" ]; then
    echo "Baixando Tomcat $TOMCAT_VER..."
    curl -fL "https://archive.apache.org/dist/tomcat/tomcat-11/v$TOMCAT_VER/bin/apache-tomcat-$TOMCAT_VER.tar.gz" -o "$FERR/tomcat.tar.gz"
    tar -xzf "$FERR/tomcat.tar.gz" -C "$FERR" && rm "$FERR/tomcat.tar.gz"
    chmod +x "$TOMCAT"/bin/*.sh
  fi
fi

# ---- 4. Aviso se o MySQL nao responde ----
if ! (exec 3<>/dev/tcp/127.0.0.1/3306) 2>/dev/null; then
  echo "AVISO: nada respondendo na porta 3306. Ligue o MySQL e rode sql/schema.sql (o login vai falhar sem o banco)."
fi

# ---- 5. Compila e publica ----
echo "Compilando..."
"$MVN" -q -DskipTests clean package
rm -rf "$TOMCAT/webapps/HelpDesk" "$TOMCAT/webapps/HelpDesk.war"
cp target/HelpDesk.war "$TOMCAT/webapps/"

echo
echo "Abra no navegador:  http://localhost:8080/HelpDesk/"
echo "Para parar o servidor: Ctrl+C"
echo
exec "$TOMCAT/bin/catalina.sh" run
