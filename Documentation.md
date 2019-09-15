# Optics Simulator - school documentation

## Original (vague) goal
The original goal was to build an optical system simulation, where the user enters a system consisting of lenses, mirrors and light sources and the program calculates and shows how each ray goes through the system. There should be a flat mirror, curved mirror and a lens (ideal or using refraction or both).

## My adjusted goal
I decided to create an easy to use GUI application containing the simulation. A user would use their mouse to choose what kind of optical device they want to create and just drag to create it. The program would be calculating and showing all of the rays real-time as the user is adjusting the system. The look and feel I wanted to achieve was similar to [this website](https://ricktu288.github.io/ray-optics/simulator/).

## Planning
For easy GUI implementation I was considering either a Javascript website with p5.js library or a Java application with Processing library. Even though a website is much more accessible and thus may benefit a larger audience, I decided to go with a Java application, as I am more experienced with Java and because of the better performance. Another reason to use Java was to try using Gradle in a project. 

For the UI, I decided to go with a top panel consisting of a couple dropdowns (File - reset, save, load, undo; Sources - ray, beam, point source; Mirrors - flat, curved, ideal curved; Lens - circle, real, ideal; Other - blocker), buttons (Move, Delete) and a density slider. As you can see, some of these options were not implemented. Under that should be a window containing the simulation which should be able to move and zoom (meaning the need for screen-to-world and world-to-screen methods).

For the simulation itself I had a pretty good plan, which I mostly followed. The main idea was to keep it as stand alone from gui as possible. Another important point was to put similar objects under one interface - for example `Mirror`, `IdealLens`, `Blocker`, etc. under `Reflectable` and overall minimize copy/pasting code from other classes - that's why `shapes` package was created. This idea was also utilized when creating the GUI, notably in `UIElement` and `RaySolver` classes.

## Program 
There are two main packages - `gui` and `logic`. The `gui` package contains everything GUI-related (including visualization of the simulation), whereas the `logic` package contains the actual simulation. 

```
eu
└───medek
    └───opticssimulator
        ├───gui
        │   ├───elements
        │   └───visualization
        │       ├───interfaces
        │       ├───prefabs
        │       ├───rays
        │       └───reflectables
        └───logic
            ├───rays
            └───reflectables
                └───shapes
```

### Package `logic`
In the `logic` package, there are two packages `rays` and `reflectables` and two classes `Vector` and `Response`. The `logic` package does not use the `Processing` library and does not rely on `gui` package in any way, meaning that it can be used in other visualizations.
* The `Vector` class just contains a 2D vector (coordinates saved as `double`) and several useful methods. 
* The `Response` class is used to return data about an impact - it contains information whether there has been an impact (`boolean impact`), if so at what position (`Vector pointOfImpact`), with which reflectable (`Reflectable objectHit`) and the resulting rays (`List<Ray> resultingRays`).
    * Empty constructor sets `impact` to `false` and everything else to `null` ("empty response")
    * The other constructor sets `impact` to `true` and everything else from the parameters.

#### Package `rays`
Package `rays` contains three classes `Beam`, `PointSource` and `Ray` and one interface `DensityListener`.

* `Ray` is simply an object containing the light ray's starting `position`, `angle` and `strength` and most importantly a method `solveReflectables`. A list of `Reflectable` objects are passed to this method. The method finds the closest impact between the `Ray` and one of the `Reflectable` objects and returns in using the `Response` object.
* `Beam` is an object managing multiple `Ray` objects with starting position on a line with `pointA` and `pointB` used as endpoints. It creates multiple perpendicular rays pointing to the right from the direction vector `pointA` to `pointB`. The count is calculated using distance between `pointA` and `pointB` and `density`.
* `PointSource` is an object managing multiple `Ray` objects starting at `PointSource`'s `position` and shooting in all directions, with the count calculated from `density`.
* `DensityListener` is an interface implemented by classes containing the `setDensity` method (`Beam` and `PointSource`). This listener is not used in this project (aside from tests), but could be useful if you want to keep a list of objects that need to react for changing the density (e.g. in another GUI implementation).

#### Package `reflectables`
This package contains one package `shapes`, one interface `Reflectable` and five classes representing different optical devices.

* The `Reflectable` interface contains two methods `getIntersection` and `getImpactResult`. 
    * Method `getIntersection` gets a `Ray` object as an argument and calculates the intersection between the ray and the class implementing this method, which it then returns as a `Vector`. If there is no intersection, method will return `null`.
    * Method `getImpactResult` also gets a `Ray` object as an argument, calls `getIntersection` to find if there is any intersection. If there is, it calculates the resulting rays (depends on the specific implementation) and returns a `Response` object containing all the relevant information. If there is no impact, method returns "empty response" (`Response` object with `impact` variable set to `false`).
* Package `shapes` contains two abstract classes `LineSegment` and `Circle`. 
    * These classes partially implement the `Reflectable` interface by implementing the `getIntersection` method calculating either a [line-line intersection](https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection) or a [line-circle intersection](http://mathworld.wolfram.com/Circle-LineIntersection.html).
    * `LineSegment` (and its children) is specified by two `Vector` endpoints `pointA` and `pointB`.
    * `Circle` (and its children) is specified by `Vector` `center` and `radius`.
* Each of the five classes is a child of either `Circle` (`CircleGlass`) or `LineSegment` (`BlockerLine`, `IdealCurvedMirror`, `IdealLens`, `Mirror`) and implements the `getImpactResult` method based on what type of optical device it is. `CircleGlass`'s constructor has an extra parameter `refractiveIndex` and `IdealLens`'s and `IdealCurvedMirror`'s constructor have an extra parameter `focusDistance`.
    * `CircleGlass` implements `getImpactResult` using [Snell's law](https://en.wikipedia.org/wiki/Snell%27s_law) and `Circle`'s `getIntersection` method.
    * `IdealLens` implements `getImpactResult` using [ideal lens equation](https://www.sciencedirect.com/science/article/pii/) and `LineSegment`'s `getIntersection` method.
    * `Mirror` implements `getImpactResult` using a bit of math and `LineSegment`'s `getIntersection` method.
    * `BlockerLine`'s implementation of `getImpactResult` always returns empty `resultingRays` list (or "empty response" in case `LineSegment`'s `getIntersection` method returned `null`).
    * `IdealCurvedMirror` extends `IdealLens` and it's implementation of `getImpactResult` returns the result of `IdealLens`'s `getImpactResult` as if reflected from a normal mirror.

### Package `gui`
In the `gui` package, there are two packages `elements` and `visualization` and two classes `GUIEntry` and `Constants`. 
* The `GUIEntry` class is the entry point of the application. It sets up the window and both the UI (using `UIController`) and the visualization (using `VisController`). It listens for the `draw`, `mousePressed`, `mouseReleased` and `mouseWheel` calls from `Processing` and relays them to `UIController` and `VISController`.
* The `Constants` class contains constants used in the `gui` package (either in `GUIEntry` class or `visualization` package)
    * `GRAB_RADIUS` is a constant that defines how for can a mouse be from an endpoint of some optical device to be able to move/delete/select it. It also sets the size of the circle showing the grabbable area.
    * `REFLECT_LIMIT` sets a limit to how many times a single ray may go through an optical device to prevent infinite recursion. 
    * Rest of the constants are labels for the buttons/dropdowns/slider used in the UI. They are used in GUIEntry when setting up the `UIController` and later read in `VisController` to decide on the correct action.

#### Package `elements`
The package `elements` is another "stand-alone" package that could be used in another project as it is not using anything outside of its own package (and the `Processing` library). It is a simple UI maker that supports buttons, dropdowns and sliders consisting of two interfaces (`OnChangeListener` and `OnClickListener`) and six classes (`ColorScheme`, `UIController`, `UIButton`, `UIDropdown`, `UISlider` and `UIElement`).
* `OnClickListener` is an interface containing the `onClick` method with `UIElement` parameter. The `element` parameter is set to the originating element. Classes that want to be notified when a certain `UIElement` was pressed need to implement this interface.
* `OnChangeListener` is an interface containing the `onChange` method with `UIElement` and the new value as parameters. The `element` parameter is set to the originating element and the `newValue` parameter is set to the new value of the originating element (for example `UISlider`). Classes that want to be notified when a certain `UIElement`'s value changes need to implement this interface.
* `ColorScheme` class is a convenient way to set multiple values at once. It includes `textColor`, `backgroundColor` and `strokeColor` and their hover variants (`hoverTextColor`, `hoverBackgroundColor`, `hoverStrokeColor`).
* `UIElement` is an abstract class containing most of the variables necessary for various UI elements (`position`, `dimensions`, `label`, `text`, `textSize`, `pApplet` and `colorScheme`) and a `checkHover` method which checks whether a vector is inside the UI element (based on its `position` and `dimensions`). It has three abstract methods:
    * `mousePressed` - should be called whenever a mouse was pressed, returns true if some UI has been triggered
    * `mouseReleased` - should be called whenever a mouse was released, returns true if some UI has been triggered
    * `draw` - should be called every frame to draw the element
* `UIButton`, `UIDropdown` and `UISlider` extend `UIElement` and implement its abstract methods in order to create a nice looking button, dropdown or slider.
* `UIController` is a class that makes managing UI elements easier. It contains a list of UI elements and when a `mousePressed`, `mouseReleased` or `draw` method is called it relays that call to all of its elements. Another use case is to use `UIController` as `UIElement` factory - that way, user just sets the `defaultStartingPoint`, `defaultColorScheme`, `defaultHeight`, `defaultSpace` and `defaultTextSize` once by calling `setupFactory` method and then, when they want to create a `UIButton`/`UIDropdown`/`UISlider`, they just need to provide a `label` and `text`, final `width` will be calculated automatically from the `text` and `defaultTextSize` (note: for the `UISlider` you also need to specify width of the slider, width of the `text` will be added automatically).

#### Package `visualization`
The package `visualization` contains classes that take care of visualizing the simulation. It consists of one class (`VisController`) and 4 packages (`interfaces`, `prefabs`, `rays` and `reflectables`).
* The `VisController` class serves as a main adapter between the user and the simulation. It implements the `OnClickListener` and `OnChangeListener` from `elements` package and based on the input from the buttons sets an appropriate action. It takes care of zoom and moving the simulation view. It displays the current action name (`Action: <current-action>`) and settings slider (when the right object is selected). It creates/removes the optical devices based on the current action.
* The `interfaces` package contains three interfaces used by other classes:
    * `MouseListeners` interface contains `mousePressed` method with the location of mouse in world coordinates as parameter and `mouseReleased` method. Both methods return boolean value and should return true if some UI has been triggered.
    * `VisCastable` interface extends `MouseListeners` interface with `update` (list of objects implementing `Reflectable` and mouse position in world coordinates as parameters) and `updateScreenEdges` (vectors specifying edges of the screen in world coordinates as parameters) methods. The `update` method should cast and draw ray/s. The `updateScreenEdges` method should update the location of the blockers at the edge of the screen to the new screen edge (in world coordinates). It is necessary because of the way zoom and move view works. This interface should be implemented by classes visualizing the package `rays`.
    * `VisReflectable` interface extends `MouseListeners` interface with `update` (mouse position in world coordinates as parameter) and `getReflectable` methods. The `update` method should draw the reflectable object. The `getReflectable` method should return the corresponding `Reflectable` object. This interface should be implemented by classes visualizing the package `reflectables`.
* The `prefabs` package contains two classes responsible for drawing and moving endpoint/s of either rays or reflectables:
    * `Vis1PointObject` maintains one point.
    * `Vis2PointObject` maintains two points.
* The `rays` package contains the visualization of rays. It has three visualization classes (`VisBeam`, `VisPointSource` and `VisRay`) and one helper class (`RaySolver`).
    * `VisBeam` class extends the `Vis2PointObject`, implements `VisCastable` interface and takes care of visualizing the `Beam` object.
    * `VisPointSource` class extends the `Vis1PointObject`, implements `VisCastable` interface and takes care of visualizing the `PointSource` object.
    * `VisRay` class extends the `Vis2PointObject`, implements `VisCastable` interface and takes care of visualizing the `Ray` object.
    * Finally, the `RaySolver` class is used by all of the above to recursively calculate and draw the ray's path (and if the ray was to exit out of the screen, not hitting anything else, it uses the `screenEdgeBlockers` to find the position where it exits the screen).
* The `reflectables` package contains the visualization of objects implementing `Reflectable`. It has five classes (`VisBlockerLine`, `VisCircleGlass`, `VisIdealCurvedMirror`, `VisIdealLens` and `VisMirror`). All of them extend `Vis2PointObject` and implement `VisReflectable` interface.
    * `VisMirror` and `VisBlockerLine` visualize mirror (blue line segment) and blocker (red line segment).
    * `VisIdealLens` and `VisIdealCurvedMirror` visualize ideal lens (light gray line segment with blue circles at the points of focus) and ideal curved mirror (blue line segment with blue circles at the points of focus) and contain setter and getter for `focusDistance`.
    * `VisCircleGlass` visualizes the circle shaped glass (blue circle with the transparency representing the refractive index) and contains setter and getter for `refractiveIndex`.


### Entire structure
```
eu
└───medek
    └───opticssimulator
        ├───gui
        │   │   Constants.java
        │   │   GUIEntry.java
        │   │
        │   ├───elements
        │   │       ColorScheme.java
        │   │       OnChangeListener.java
        │   │       OnClickListener.java
        │   │       UIButton.java
        │   │       UIController.java
        │   │       UIDropdown.java
        │   │       UIElement.java
        │   │       UISlider.java
        │   │
        │   └───visualization
        │       │   VisController.java
        │       │
        │       ├───interfaces
        │       │       MouseListeners.java
        │       │       VisCastable.java
        │       │       VisReflectable.java
        │       │
        │       ├───prefabs
        │       │       Vis1PointObject.java
        │       │       Vis2PointObject.java
        │       │
        │       ├───rays
        │       │       RaySolver.java
        │       │       VisBeam.java
        │       │       VisPointSource.java
        │       │       VisRay.java
        │       │
        │       └───reflectables
        │               VisBlockerLine.java
        │               VisCircleGlass.java
        │               VisIdealCurvedMirror.java
        │               VisIdealLens.java
        │               VisMirror.java
        │
        └───logic
            │   Response.java
            │   Vector.java
            │
            ├───rays
            │       Beam.java
            │       DensityListener.java
            │       PointSource.java
            │       Ray.java
            │
            └───reflectables
                │   BlockerLine.java
                │   CircleGlass.java
                │   IdealCurvedMirror.java
                │   IdealLens.java
                │   Mirror.java
                │   Reflectable.java
                │
                └───shapes
                        Circle.java
                        LineSegment.java
```

## Tests
* Several tests were created during the creation of this project. All of them are testing the `logic` package, some are automatic other manual.
* There are automatic tests for `LineSegment`, `BlockerLine`, `Mirror`, `Ray`, `Response` and `Vector` classes. 
* The rest of the classes from the `rays` package is tested using manual test `ManualRaysPackage`. 
* `IdealLens` and `IdealCurvedMirror` are tested using manual test `ManualLineSegmentChildren` (you need to change one line to test the requested `LineSegment` child).
* `CircleGlass` and `Circle` are tested using manual test `ManualCircleGlass`.
* The `Checks` class is used to compare specific types of values as are `double`, `Vector` or angles.
* The `gui` package can be manually tested by running the finished application and trying various edge cases (not described anywhere).

```
eu
└───medek
    └───opticssimulator
        │   Checks.java
        │
        └───logic
            │   ManualCircleGlass.java
            │   ManualLineSegmentChildren.java
            │   ManualRaysPackage.java
            │   RayTest.java
            │   ResponseTest.java
            │   VectorTest.java
            │
            └───reflectables
                │   BlockerLineTest.java
                │   MirrorTest.java
                │
                └───shapes
                        LineSegmentTest.java
```

## What is missing & Possible future steps
As specified in "Problems & Experience", there is really no way of getting the refractive index at some point in the simulation. This also means that overlapping glass circles are not simulated correctly. One solution is to set the refractive index in `Ray`. That way, the only place where the application would need to change is when the ray is created (application needs to know the refractive index at that point - that means going through all `Reflectable` objects), because later it would only change on the edge of optical devices. 

The project is ready for both more rays to be created by one intersection and different strength of rays - this could be useful especially with the circle glass as in real life, part of the light enters the glass and part reflects. Unfortunately the math for calculating the percentages is rather hard, so I decided to leave it out for now.

Another use for different strength of rays is simply to be able to set what the strength of a certain light source is.

One of possible future steps is to improve performance. The main slowdown comes from the GUI of the simulation, but there is a way to speed that up - by changing to a different renderer in `Processing`. It is much faster (at least 5-10x from my testing) and uses OpenGL. The reason why I didn't use it so far is that it comes with some disadvantages - for example worse image quality, longer load times or even supposedly not working on some systems altogether. 

Another step to possibly improve performance is to reuse objects instead of creating new ones each time an optical device moves (e.g. rays in `Beam`). Whether this will help probably depends on the Java's garbage collector. 

I would also like to add other functions such as `Save`, `Load` (shouldn't be that hard) and possibly `Undo` (harder) under `File` and other optical devices, for example an actual curved mirror and real lens (using Snell's law) as planned in the "Planning" section.

Last improvement, or even a beginning of another project, is improving the UI maker. Right now it can work completely standalone, meaning that with a little improvement, it could be used for other projects as well. It would also be nice to add other UI elements like labels, text fields, etc.

## Problems & Experience
One of the biggest problems was the physics itself, mainly finding good resources (especially for the ideal lens equation) and also figuring out the correct angles - there was a surprising amount of math involved considering this was a programming assignment.

Main hinderance for the possible future development of this project is performance. The program is certainly usable without any problems unless you either increase the ray density by a lot or create a system with infinite reflections (program limits the number of reflections to 1000, but that is still a lot, especially with high ray density). Surprisingly (to me at least) the simulation part is fast enough, but the visualization could use an improvement. Fortunately, as mentioned above, there is a way in `Processing` to change to a different renderer.

Some of the problems I got stuck on several times were created by a rather vague planning. This can be seen in the `RaySolver` class and other classes using it. The `updateScreenEdges` method was "hacked in" after realizing that with the current setup, all `Reflectable` objects operated on world coordinates, meaning the screen edges needed to be set to world coordinates too. The problem being that the only class that had access to screen edges in world coordinates was `VisController`. This didn't matter until the last moment when I started implementing move and zoom as the screen and world coordinates matched each other. 

Another problem which originated from vague planning was when I was trying to use Snell's law to calculate the angle of resulting ray after entering the circle glass. You need the refractive index of both inside and outside environment - I did have the index of the inside environment, but couldn't get the outside one since I would have to check all the `Reflectable` objects meaning a possible slowdown. I decided to just go with setting the outside one to one and recommending not to let more circle glass objects overlap.

## Resources & Other info
* [Processing 3](https://processing.org/download/) - the graphical library used in this project
* [line-line intersection](https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection)
* [line-circle intersection](http://mathworld.wolfram.com/Circle-LineIntersection.html)
* [ideal lens equation](https://www.sciencedirect.com/science/article/pii/S0030402615000364#eq0005)
* [Snell's law](https://en.wikipedia.org/wiki/Snell%27s_law)

Toto je zápočtový program vytvořený Jakubem Medkem během letního semestru 2018/19 I. ročníku IOI MFF UK pro předmět Programování II (NPRG031).