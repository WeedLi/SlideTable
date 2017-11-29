# SlideTable

[![](https://jitpack.io/v/WeedLi/SlideTable.svg)](https://jitpack.io/#WeedLi/SlideTable)

- 支持自定义表格内容
- 支持上下左右滑动 表格item的点击
- 支持表格中item的左右边距的调整 支持item自动归位
- 支持滑动到指定位置

### 缺点

- 如果列数（水平方向的item）过多，测试过超过50，滑动会卡顿

### APK包

* [slideTable.apk](https://github.com/WeedLi/SlideTable/blob/master/slideTable.apk)

### 效果图

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

#### XML

```xml
 <com.leo.tablelibrary.SlideTableView
        android:id="@+id/slideTable"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```

##### 实现 ITableAdapter

* [TestTableAdapter](https://github.com/WeedLi/SlideTable/blob/master/app/src/main/java/com/leo/monthtable/TestTableAdapter.java)


##### 设置Adapter

```java
  SlideTableView slideTableView = (SlideTableView) findViewById(R.id.slideTable);
  slideTableView.setTableAdapter(new TestTableAdapter(this, dateData, staffData, contentData));
  //滑动到指定位置
  slideTableView.slideToSomeColAndRow(col, col);
  //定义第一个表格
  slideTableView.addFirstView(LayoutInflater.from(this).inflate(R.layout.item_first_view, null, false));
        
 ```


## Credits

* [recyclerview-playground](https://github.com/devunwired/recyclerview-playground)
