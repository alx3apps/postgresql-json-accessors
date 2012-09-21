#!/bin/bash
set -e
CURRENT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
VERSION="1.2"
GSON_VERSION="2.2.1"

PG_DIR="PROVIDEME"
PG_SCHEMA="public"
UNINSTALL="false"

CLASSPATH_INSTALL_FILE="$CURRENT_DIR"/classpath_install.sql

usage()
{
cat << EOF
usage: $0 [options]

PostgreSQL json_accessors_java extension install script

OPTIONS:
    -d  PostgreSQL installation directory, must be provided
    -s  PostgreSQL schema name, 'public' by default
    -u  uninstall extension from PostgreSQL directory
    -h  show this message
EOF
}

classpath_file() {
cat << EOF
-- json_accessors_java classpath installation file

-- Run these commands as superuser (run commented commands on reinstallation):
-- select sqlj.remove_jar('gson', false);
select sqlj.install_jar('file://$LIB_DIR/json_accessors_java/gson-$GSON_VERSION.jar', 'gson', false);
-- select sqlj.remove_jar('json_accessors_java', false);
select sqlj.install_jar('file://$LIB_DIR/json_accessors_java/postgresql-json-accessors-$VERSION.jar', 'json_accessors_java', false);
select sqlj.set_classpath('$PG_SCHEMA', 'gson:json_accessors_java');

-- Reconnect as normal to apply classpath updating. You can use next commands to manage extension.
-- create extension json_accessors_java
-- drop extension json_accessors_java cascade
EOF
}

install() {
echo "Creating extension directory: '""$LIB_DIR"/json_accessors_java"'..."
mkdir "$LIB_DIR"/json_accessors_java

echo "Copying libraries to extension directory..."
cp postgresql-json-accessors-"$VERSION".jar "$LIB_DIR"/json_accessors_java
cp gson-"$GSON_VERSION".jar "$LIB_DIR"/json_accessors_java

echo "Copying extension descriptors..."
cp json_accessors_java--"$VERSION".sql "$EXT_DIR"
cp json_accessors_java.control "$EXT_DIR"

echo "Preparing file classpath_install.sql..."
touch "$CLASSPATH_INSTALL_FILE"
classpath_file > "$CLASSPATH_INSTALL_FILE"

echo "Installation complete, apply 'classpath_install.sql' as superuser (psql ... -f classpath_install.sql)"
echo "See 'classpath_install.sql' comments for further instructions"
}

uninstall() {
    echo "Removing extension directory: '""$LIB_DIR"/json_accessors_java"'..."
    rm -rf "$LIB_DIR"/json_accessors_java
    echo "Removing extension descriptors..."
    rm "$EXT_DIR"/json_accessors_java--"$VERSION".sql
    rm "$EXT_DIR"/json_accessors_java.control
    echo "Removing classpath_install.sql..."
    rm $CLASSPATH_INSTALL_FILE
    echo "Uninstallation complete"
}


while getopts ":d:suh" OPTION
do
    case $OPTION in
        d)
            PG_DIR=$OPTARG
            ;;
        s)
            PG_SCHEMA=$OPTARG
            ;;
        u)
            UNINSTALL="true"
            ;;
        h)
            usage
            exit
            ;;
        ?)
            usage
            exit
            ;;
     esac
done

echo "Using PostgreSQL installation directory: '"$PG_DIR"'"

LIB_DIR="$PG_DIR"/lib/postgresql
echo "Using PostgreSQL library directory: '"$LIB_DIR"'"
if [ ! -d "$LIB_DIR" ]; then
    echo "Library directory "$LIB_DIR" doesn't exist, check input parameter" 1>&2
    usage
    exit
fi

EXT_DIR="$PG_DIR"/share/postgresql/extension
echo "Using PostgreSQL extension directory: '"$EXT_DIR"'"
if [ ! -d "$EXT_DIR" ]; then
    echo "Extension directory "$EXT_DIR" doesn't exist, check input parameter" 1>&2
    usage
    exit
fi

if [ "$UNINSTALL" = "true" ] ; then
    uninstall
else
    install
fi
