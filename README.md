
# Project Title
Spring AOP logger


## Acknowledgements

 - [Spring AOP](https://docs.spring.io/spring-framework/docs/2.5.x/reference/aop.html)

## Authors

- [Mahmud Bodurov](https://www.github.com/Mahmud989)


## Features

- logs parameters
- hide parameters
- mark parameters


## 🚀 About 
Logging tool uses spring aop

## Publishing
 ``` ./gradlew clean build publish -DprojectVersion=1.0.7 -DrootProjectName=logger -PrepoPassword=```

## Usage/Examples

```java

import com.mb.tool.model.annotation.Confidential;
import com.mb.tool.model.annotation.Log;
import com.mb.tool.model.constant.LogConstants;
import org.springframework.stereotype.Service;

@Service
public class ExampleService {

    @Log(suffix = "ExampleService")
    public void doSomeStuff(String plainData,
                            @Confidential String hiddenData,
                            @Confidential(level = LogConstants.CONFIDENTIAL_REMARK) String remarkedData) {
        System.out.println("plainData: " + plainData);
        System.out.println("hiddenData: " + hiddenData);
        System.out.println("remarkedData: " + remarkedData);
    }
}
```


## Tech Stack
 Aspectj, Spring Boot, Spring AOP



