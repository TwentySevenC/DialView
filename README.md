## DialView
DialView

A dial viw

## Screenshot
<img src="https://raw.githubusercontent.com/TwentySevenC/DialView/master/app/dialview.gif" width="350px">

## How to use
```
<declare-styleable name="DialView">
        <attr name="dv_outerBorderWidth" format="dimension"/>   <!--outer border width-->
        <attr name="dv_mediumBorderWidth" format="dimension"/>  <!--medium border width-->
        <attr name="dv_innerBorderWidth" format="dimension"/>   <!--inner border width-->
        <attr name="dv_borderColor" format="color"/>            <!--border color-->
        <attr name="dv_fingerColor" format="color"/>            <!--finger color-->
        <attr name="dv_borderPadding" format="dimension"/>      <!--boader padding-->
        <attr name="dv_startAngle" format="integer"/>           <!--start angle-->
        <attr name="dv_endAngle" format="integer"/>             <!--end angle-->
        <attr name="dv_progress" format="float"/>               <!--finger progress-->
</declare-styleable>
    
    
//finger smoothly scroll to the progress value with a animation
//progress value from 0 to 1.0
dialView.setProgressWithAnimation(progressValue);
  
dialView.setProgress(progressValue)//no animation
  
```    
