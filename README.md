# SlideTable

[![](https://jitpack.io/v/WeedLi/SlideTable.svg)](https://jitpack.io/#WeedLi/SlideTable)

- 支持上下左右滑动 表格item的点击
- 支持表格中item的左右边距的调整 支持item自动归位

## 缺点

- 如果列数（水平方向的item）过多，测试过超过50，滑动会卡顿

## 效果图

<img src="https://github.com/WeedLi/SlideTable/blob/master/leoslidetable.gif" alt="Demo" height="600px"/>

## Setup

To use this library your `minSdkVersion` must be >= 19.

In your project level build.gradle :
```java
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}       
```

In your app level build.gradle :
```java
dependencies {
   compile 'com.github.WeedLi:SlideTable:1'
}      
```

## Credits

* [recyclerview-playground](https://github.com/devunwired/recyclerview-playground)
