#### Stampa argv
```java
public class Main {  
    public static void main(String[] args) {  
        for (String s:  args) {  
            System.out.println(s);  
        }  
    }  
}
```
#### Check directory o file
```java
import java.io.*;  
  
public class Main {  
    public static void main(String[] args) {  
        for (String path:  args) {  
            File f = new File(path);  
            if (f.isDirectory()) {  
                System.out.println("Is directory");  
            } else {  
                System.out.println("Is not directory");  
            }  
        }  
    }  
}
```
