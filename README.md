# New AppWidget in Android 12
New AppWidget demo that used with [AppWidget](https://developer.android.google.cn/about/versions/12/features/widgets) API in `Android 12`.

![](https://z3.ax1x.com/2021/06/01/2KnOVs.png)

## :camera_flash:Screenshot
### Rounded corner AppWidget
<div align=center><img src="https://img-blog.csdnimg.cn/20210530223800395.png" alt="12-widget" height="50%" width="50%"></div>

### Dynamic color effect
<div align=center><img src="https://z3.ax1x.com/2021/06/01/2KFt2j.gif" alt="12-widget" height="50%" width="50%"></div>

### Responsive layouts
<div align=center><img src="https://z3.ax1x.com/2021/05/30/2Zmh5D.gif" alt="12-widget" height="50%" width="50%"></div>

### State View AppWidget
<div align=center><img src="https://z3.ax1x.com/2021/05/30/2ZmE3d.gif" alt="12-widget" height="50%" width="50%"></div>

### Simplified RemoteViews collections
<div align=center><img src="https://z3.ax1x.com/2021/05/30/2ZKcKs.png" alt="CheckBox精准布局预览" height="60%" width="60%"></div>

### Runtime modification of RemoteViews
Change checkbox text color when checked.
<div align=center><img src="https://z3.ax1x.com/2021/05/30/2ZmHKI.gif" alt="12-widget" height="50%" width="50%"></div>

Change chart's size when tapped.
<div align=center><img src="https://z3.ax1x.com/2021/06/01/2Ke8I0.gif" alt="12-widget" height="50%" width="50%"></div>

## :orange_book:　New AppWidget API

### RemoteViews类
| 方法                                          | 作用                                                 |
| --------------------------------------------- | ---------------------------------------------------- |
| RemoteViews(Map<SizeF, RemoteViews>)          | 根据响应式布局映射表创建目标RemoteViews              |
| addStableView()                               | 向RemoteViews动态添加子View，类似ViewGroup#addView() |
| setCompoundButtonChecked()                    | 针对CheckBox或Switch控件更新选中状态                 |
| setRadioGroupChecked()                        | 针对RadioButton控件更新选中状态                      |
| setRemoteAdapter(int , RemoteCollectionItems) | 直接将数据填充进小组件的ListView                     |
| setColorStateList()                           | 动态更新小组件视图的颜色                             |
| setViewLayoutMargin()                         | 动态更新小组件视图的边距                             |
| setViewLayoutWidth()、setViewLayoutHeight()   | 动态更新小组件视图的宽高                             |
| setOnCheckedChangeResponse()                  | 监听CheckBox等三种状态小部件的状态变化               |

### XML属性

| 属性                              | 作用                                               |
| --------------------------------- | -------------------------------------------------- |
| description                       | 配置小组件在选择器里的补充描述                     |
| previewLayout                     | 配置小组件的预览布局                               |
| reconfigurable                    | 指定小组件的尺寸支持直接调节                       |
| configuration_optional            | 指定小组件的内容可以采用默认设计，无需启动配置画面 |
| targetCellWidth、targetCellHeight | 限定小组件所占的Launcher单元格                     |
| maxResizeWidth、maxResizeHeight   | 配置小组件所能支持的最大高宽尺寸                   |

## :orange_book:　New AppWidget Feature
<https://developer.android.google.cn/about/versions/12/features/widgets>

## :orange_book:　My blog

<https://blog.csdn.net/allisonchen>

## :copyright: License
```
MIT License

Copyright (c) 2021 Ellison Chan

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

```