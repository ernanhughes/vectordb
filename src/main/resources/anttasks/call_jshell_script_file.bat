# -R will pass arguments for runtime. In this sample we pass -D and it sets system property "args"
# to value 'Some arg with spaces' $SHELL $TERM some_other_arg
#
set ANT_HOME=A:/java/apache-ant-1.10.13
jshell --class-path %ANT_HOME%/lib/ant.jar -R-Dargs="'Some arg with spaces' $SHELL $TERM some_other_arg" jshell_script_file


jshell --class-path A:/projects/vectordb/target/vectordb-1.0-SNAPSHOT.jar  -R-Dargs="'Some arg with spaces 2' $SHELL $TERM some_other_arg" jshell_script_file