Missing support for `module-name/bean-name` syntax in `@DependsOn`

Started here:
- https://www.eclipse.org/lists/ejb-dev/msg00321.html

I understand that final conclusion is, that `ejb-link` syntax, including `module-name/bean-name`, should be supported in `@DependsOn`.

Steps to reproduce:

1. In first console - build EARchives:
```
git clone https://github.com/pzrep/ejb-depends-on-module-name
cd ejb-depends-on-module-name
./mvnw clean package
```

2. In second console - start OpenLiberty:
```
wget https://repo.maven.apache.org/maven2/io/openliberty/openliberty-jakartaee10/25.0.0.6/openliberty-jakartaee10-25.0.0.6.zip
unzip -q openliberty-jakartaee10-25.0.0.6.zip
cd wlp
pwd # LIBERTY_HOME
bin/server run
```

3. In third console - start GlassFish:
```
wget https://repo.maven.apache.org/maven2/org/glassfish/main/distributions/glassfish/7.0.25/glassfish-7.0.25.zip
unzip -q glassfish-7.0.25.zip
cd glassfish7
pwd # GLASSFISH_HOME
glassfish/bin/asadmin start-domain --verbose
```

4. In first console - deploy:
```
cp ./ejb-singletons.ears/ejb-singletons.file-name-ear/target/ejb-singletons.file-name-ear-1.0-SNAPSHOT.ear ${LIBERTY_HOME}/usr/servers/defaultServer/dropins
cp ./ejb-singletons.ears/ejb-singletons.file-name-ear/target/ejb-singletons.file-name-ear-1.0-SNAPSHOT.ear ${GLASSFISH_HOME}/glassfish/domains/domain1/autodeploy
```

5. Once deployed check output of:
```
# OpenLiberty:
curl http://localhost:9080/fn/inspector
# GlassFish:
curl http://localhost:8080/fn/inspector
```
Expected: the same.

6. In first console - deploy, or try to deploy:
```
cp ./ejb-singletons.ears/ejb-singletons.module-name-ear/target/ejb-singletons.module-name-ear-1.0-SNAPSHOT.ear ${LIBERTY_HOME}/usr/servers/defaultServer/dropins
cp ./ejb-singletons.ears/ejb-singletons.module-name-ear/target/ejb-singletons.module-name-ear-1.0-SNAPSHOT.ear ${GLASSFISH_HOME}/glassfish/domains/domain1/autodeploy
```

7. Once deployed check output of (OpenLiberty only, as GlassFish rejects archive):
```
# OpenLiberty:
curl http://localhost:9080/mn/inspector
```

8. Differences between EARs:

   a. EARs include different ejb-jar - `ejb-file-name` or `ejb-module-name`:
```
diff -U3 ejb-singletons.ears/ejb-singletons.file-name-ear/pom.xml ejb-singletons.ears/ejb-singletons.module-name-ear/pom.xml
```
```diff
--- ejb-singletons.ears/ejb-singletons.file-name-ear/pom.xm
+++ ejb-singletons.ears/ejb-singletons.module-name-ear/pom.xml
@@ -8,7 +8,7 @@
         <version>1.0-SNAPSHOT</version>
     </parent>
 
-    <artifactId>ejb-singletons.file-name-ear</artifactId>
+    <artifactId>ejb-singletons.module-name-ear</artifactId>
     <packaging>ear</packaging>
 
     <dependencies>
@@ -23,7 +23,7 @@
         </dependency>
         <dependency>
             <groupId>pzrep.ejb-depends-on-module-name</groupId>
-            <artifactId>ejb-singletons.ejb-file-name</artifactId>
+            <artifactId>ejb-singletons.ejb-module-name</artifactId>
             <type>ejb</type>
         </dependency>
         <dependency>
@@ -42,7 +42,7 @@
                         <webModule>
                             <groupId>pzrep.ejb-depends-on-module-name</groupId>
                             <artifactId>ejb-singletons.war</artifactId>
-                            <contextRoot>/fn</contextRoot>
+                            <contextRoot>/mn</contextRoot>
                         </webModule>
                     </modules>
                 </configuration>
```

   b. The modules differ in the way the `@DependsOn` is expressed:
```
diff -U3 ejb-singletons.ejbs/ejb-singletons.ejb-file-name/src/main/java/pzrep/ejblinksyntax/efn/Choco.java ejb-singletons.ejbs/ejb-singletons.ejb-module-name/src/main/java/pzrep/ejblinksyntax/ebn/Choco.java
```
```diff
--- ejb-singletons.ejbs/ejb-singletons.ejb-file-name/src/main/java/pzrep/ejblinksyntax/efn/Choco.java
+++ ejb-singletons.ejbs/ejb-singletons.ejb-module-name/src/main/java/pzrep/ejblinksyntax/ebn/Choco.java
@@ -1,4 +1,4 @@
-package pzrep.ejblinksyntax.efn;
+package pzrep.ejblinksyntax.ebn;
 
 import jakarta.ejb.DependsOn;
 import jakarta.ejb.Singleton;
@@ -8,11 +8,11 @@
 @Singleton
 @Startup
 @DependsOn({
-        "pzrep.ejb-depends-on-module-name-ejb-singletons.ejb-base-1.0-SNAPSHOT.jar#Ant"
+        "Base/Ant"
 })
 public class Choco implements Cat {
     @Override
     public String color() {
-        return "brown (file-name#)";
+        return "brown (module-name/)";
     }
 }
```

