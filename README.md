[![](https://jitpack.io/v/Doris1112/IBase.svg)](https://jitpack.io/#Doris1112/IBase)
### 步骤1
```
allprojects {
      repositories {
        ...
        maven { url 'https://jitpack.io' }
      }
}
```
或者
```
<repositories>
      <repository>
          <id>jitpack.io</id>
          <url>https://jitpack.io</url>
      </repository>
</repositories>
```

### 步骤2
```
dependencies {
         implementation 'com.github.Doris1112:IBase:1.0.2'
}
```
或者
```
<dependency>
        <groupId>com.github.Doris1112</groupId>
        <artifactId>IBase</artifactId>
        <version>1.0.2</version>
</dependency>
```
