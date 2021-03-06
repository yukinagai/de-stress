## Wearable Bath
This system has been developed for Space Apps Challenge 2016 ( https://2016.spaceappschallenge.org/challenges/space-station/rock-it-space-fashion-and-design/projects/wearable-bath ).

### Motivation

There is no bath or shower in the ISS and rockets to Mars. No gravity makes it difficult to drain splash completely, so that water drifts in room and clings on the wall, which is very dangerous for astronauts/cosmonauts and machineries in the rocket. On the other hand, soaking in a bath/taking a shower is really good thing for relaxing and, of course, keeping sanitary. Although, wet towel and dry shampoo are used in the ISS, we imagine that it is far from perfect.

Our team “de-stress” solves “Rock-It Space Fashion and Design” to care physical and mental health for astronauts and cosmonauts. We propose a wearable bath, which is a total bath system. The most significant problem on taking a shower in the ISS is that the crew must clean up (i.e., wipe up) the splash. It must be very hard work, and so, no one wanted to take a shower. We consider that bath is suitable in the space because shower generate splash. However, a bath tub would not work in the environment of no gravity. So, we came up with a slim bath suits that covers the crew’s body and hot water is inside the suits. We put emphasis on “after finishing bath”. We devised a way to achieve the situation that the body after putting off the suits has little splash. This means that no splash spread out, so that no clean up is needed. We believe that the refreshment for the crews will improve their performance and leads to success in their missions.

## Explanation

The wearable bath consists of a bath suits, vital sensors, a fan and hands-free controller. 

<img src="https://github.com/yukinagai/de-stress/blob/master/img/wearable_bath.png" alt="concept image" width="400"/>
<img src="https://github.com/yukinagai/de-stress/blob/master/img/wearable_bath_system.png" alt="system image" width="400"/>

### Bath suits
The bath suits will be made of silicon or something waterproof and soft materials for full body below neck. It is equipped with pouring/draining/ventilation tubes for hot and warm water and drying the body roughly. To avoid suffocation and overflow from the suits, the suits has a double-layered towel and jet air dryers at round the neck. The air dryers blow off the water streaming up his/her neck. The inner layer of the towel attached to the inside of the suits sucks up water not only slipping by the air dryers but also clinging to the inside of the suits. The outer layer of the towel attached to the outside of the suits wipe up the water on his/her body when he/she put off the suits. The jet air dryers also spew out warm air and then the air warm up the water clings to his/her body.

### Prototype System Overview
The system around the bath suite allows a crew to enjoy bath comfortably and safely. The vital information is monitored using sensors to notify when to finish soaking. The fan is for cooling face/head. He/she can control the bath related things (e.g., the temperature of the water) by shaking the head because he/she cannot use the hand to control the system when soaking in the bath. So, we developed hands-free UI using depth camera.

<img src="https://github.com/yukinagai/de-stress/blob/master/img/system_diagram.png" alt="Drawing" style="width:200px;"/>

## How to use

1. Put on bath suits (and may need to wear vital sensors)
2. Pour hot/warm water through the tubes.
3. Enjoy the bath!
    * If you feel hot, the fan will cool down your head
    * If you want change the temperature of the bath, hot or cold water flows into the suits from one tube and flows out of it from another one.
4. The system will notify you of finishing bath
5. Water in the suits is drained from the tube.
6. Warm wind blows from the ventilation tubes to dry the body roughly.
7. Put off the suits
    * The towels with the suits wipe the splash on the body. (Just the behavior of putting off is enough to do so)
    * No splash in room/on the wall
