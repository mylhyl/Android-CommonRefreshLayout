#### Android-PullRefreshLayout
基于`SwipeRefreshLayout`下拉刷新、上拉加载。支持的`AbsListView`、`RecycleView`、`WebView`

####特点
 * 在布局`layout`中使用，支持`xml`属性
 * 支持自动下拉刷新，什么用呢？比如进入界面时，只需要调用`autoRefresh()`方法即可，显示上拉动画同时刷新回调`onRefresh`将会被调用。
 * 上拉加载支持自定义`View`或设置加载文字、动画
 * 轻松设置`Adapter`空数据视图，默认为`TextView`支持更文字，也可自定义`View`
 * 对于简单的界面，例如只有`ListView`可以继承 [SwipeRefreshListFragment](commonrefreshlayout/src/main/java/com/mylhyl/crlayout/app/SwipeRefreshListFragment.java)
   轻松搞定

####效果图
<img src="preview/gif.gif" width="240px"/>

####使用
  仔细看`listSelector`属性，我们是支持的，效果见`sample`
```xml
        <com.mylhyl.crlayout.SwipeRefreshListView xmlns:android="http://schemas.android.com/apk/res/android"
            xmlns:tools="http://schemas.android.com/tools"
            android:id="@+id/swipeRefresh"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:listSelector="@drawable/selector_list"
            tools:context=".app.ListViewXmlFragment" />
```
 设置上拉加载，更多方法见 [IFooterLayout](commonrefreshlayout/src/main/java/com/mylhyl/crlayout/IFooterLayout.java)
```java
        IFooterLayout footerLayout = swipeRefreshListView.getFooterLayout();
        footerLayout.setFooterText("set自定义加载");
        footerLayout.setIndeterminateDrawable(getResources().getDrawable(R.drawable.footer_progressbar));
```
或 xml属性配置，更多属生见[attrs](commonrefreshlayout/src/main/res/values/attrs.xml)
```xml
            crl:footer_text="数据正在加载中"
            crl:footer_indeterminate_drawable="@drawable/footer_progressbar"
```

 自定义上拉加载
 
 * 方式一：注意此方法必须在`setOnListLoadListener`之前调用
 
```java
        getSwipeRefreshLayout().setFooterResource(R.layout.swipe_refresh_footer);
```
 * 方式二：xml属性配置
 
```xml
        crl:footer_layout="@layout/swipe_refresh_footer"
```
 * 方式三：继承重写`getFooterResource()`方法
 
```java
        public class MySwipeRefreshGridView extends SwipeRefreshGridView {
        
            @Override
            protected int getFooterResource() {
                return R.layout.swipe_refresh_footer;
            }
        }
```
设置adapter空数据视图文字
```java
        swipeRefreshListView.setEmptyText("数据呢？");
```
 自定义adapter空数据视图
```java
        ImageView emptyView = new ImageView(getContext());
        emptyView.setImageResource(R.mipmap.empty);
        swipeRefreshGridView.setEmptyView(emptyView);
```
注册`ListView`长按事件怎么办？好说好说提供了方法`getScrollView()`，既然能获取`ListView`那`SwipeRefreshLayout`是不是也可以获取到呢？答案是肯定的，方法`getSwipeRefreshLayout`，你可以随心所欲设置下拉圆圈的颜色、大小等。
关于更多公开方法见 [ISwipeRefresh](commonrefreshlayout/src/main/java/com/mylhyl/crlayout/ISwipeRefresh.java)、[ILoadSwipeRefresh](commonrefreshlayout/src/main/java/com/mylhyl/crlayout/ILoadSwipeRefresh.java)

####使用Gradle构建时添加一下依赖即可:
```javascript
compile 'com.mylhyl:commonrefreshlayout:1.3'
```
#### 如果使用eclipse
只能`clone`源码，然后在 eclipse 中用`library`方式引用
     
#### [下载APK体验](preview/sample-debug.apk)

### QQ交流群:435173211

#### 更新日志
> 1.0 初始版本

> 1.1 修改为`recyclerview-v7:23.4.0`

> 1.2 修复滑动到底部会上拉加载多次

> 1.3 增加autoRefresh(@ColorRes int... colorResIds)方法，指定动画颜色
