# Program for editing vector drawings
This is the program for editing vector drawings made in java

## How it works
In the toolbar there are 7 buttons.<br />
![Toolbar](assets/images/toolbar.png) <br />
When you want to draw the line or the oval, first you need to press the 
corresponding button.<br />
They will firstly look like this.<br />
![Added shapes](assets/images/added_shapes.png)<br /><br />
If you want to select a single shape you need to press the Select button and then the shape.<br />
When you select only one shape you will see its hot-points and with them you can change shapes size and orientation.<br />
You can move the selected shape by using arrow keys.<br />
You can also change its z-order, + key moves it closer to you and - key moves it further away.<br />
![Hot-points](assets/images/selection.png)<br /><br />
If you want to select more shapes you can do that by holding down CTRL key.<br />
When you have multiple shapes selected, you can merge them into one by pressing G on your keyboard and you can also unmerge them by pressing U on your keyboard.<br />
This is how merged shapes look like.<br />
![Composite](assets/images/composite.png)<br /><br />
If you want to delete some of the shapes, you need to press Eraser on the toolbar and then just press the mouse down and drag it. The mouse will leave the trail behind and if that trail goes through the shape, the shape will be deleted.<br />
Composites(merged shapes) count as only one shape, no matter how many individual shapes are in them.<br />
![Eraser](assets/images/eraser.png)<br /><br />
There is also a functionality of exporting the image in SVG format.<br />
You can also save and load the image in the custom .graph format.<br />
This format uses shape IDs and its hot-points to show them.<br />
For example:<br />
**@LINE 255 92 197 46**<br />
**@OVAL 144 123 134 133**<br />
**@LINE 112 319 207 210**<br />
**@COMP 2**<br />
**@OVAL 270 242 260 252**<br />
**@COMP 2**<br />
**@OVAL 349 108 339 118**<br />
**@LINE 324 188 404 260**<br />
**@COMP 2**<br />
Here we go line by line and read ID and hot-points. @LINE is line segment and @OVAL is oval. First two numbers are the first hot-point and the second two numbers are the second hot-point. @COMP is the composite and the number after the ID shows how many shapes before that line are inside the composite.<br /><br />
## Run the program
If you want to test the program yourself you'll need to have java installed on your computer.<br />
If you do have it, you can just run **Vector-drawings.jar** which is located in root folder of this project.
