package de.romjaki.pluggabledino.game


import org.jbox2d.callbacks.ContactImpulse
import org.jbox2d.callbacks.ContactListener
import org.jbox2d.collision.Manifold
import org.jbox2d.collision.shapes.PolygonShape
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.*
import org.jbox2d.dynamics.contacts.Contact
import org.newdawn.slick.Input
import java.util.*

class GameWorld : ContactListener {
    override fun endContact(contact: Contact?) {

    }

    override fun beginContact(contact: Contact?) {
        contact!!
        val bodies = listOf(contact.fixtureA.body, contact.fixtureB.body)
        if (bodies.contains(dino) && cacti.any { bodies.contains(it) }) {
            hurt = true

        } else if (bodies.contains(dino) && birdd.any { bodies.contains(it) }) {
            hurt = true
        }
    }

    override fun preSolve(contact: Contact?, oldManifold: Manifold?) {
    }

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {
    }

    var hurt = false

    val world: World

    val cacti = mutableListOf<Body>()

    val cactiBodyDef: BodyDef

    val birdd = mutableListOf<Body>()

    val birddBodyDef: BodyDef

    var speed = 1000

    val groundBody: Body

    val random = Random()

    var delay = 0f

    var bg = 0f

    val dino: Body

    val groundBodyDef: BodyDef

    val groundBox: PolygonShape

    val dinoDef: BodyDef

    val dinoBox: PolygonShape

    val dinoFixtureDef: FixtureDef

    init {
        val gravity = Vec2(0f, 40f)
        world = World(gravity)

        //#region GROUND
        groundBodyDef = BodyDef()
        groundBodyDef.position.set(0f, 50f)
        groundBody = world.createBody(groundBodyDef)
        groundBox = PolygonShape()
        groundBox.setAsBox(50f, 10f)
        groundBody.createFixture(groundBox, 0f)
        //#endregion

        //#region DINO
        dinoDef = BodyDef()
        dinoDef.type = BodyType.DYNAMIC
        dinoDef.position.set(4f, 39f)
        dino = world.createBody(dinoDef)
        dinoBox = PolygonShape()
        dinoBox.setAsBox(1f, 1f)
        dinoFixtureDef = FixtureDef()
        dinoFixtureDef.shape = dinoBox
        dinoFixtureDef.density = 0.9f
        dinoFixtureDef.friction = 0.3f
        dino.createFixture(dinoFixtureDef)
        //#endregion

        cactiBodyDef = BodyDef()
        cactiBodyDef.type = BodyType.KINEMATIC

        birddBodyDef = BodyDef()
        birddBodyDef.type = BodyType.KINEMATIC

        createCactus1()
        createBird1()
    }


    fun rand(from: Int, to: Int): Int {
        return random.nextInt(to - from) + from
    }

    fun createBird1() {


        val body = world.createBody(birddBodyDef)
        body.position.set(50f, 33f)
        val shape = PolygonShape()
        shape.setAsBox(1f, 1f)
        val birddFixtureDef = FixtureDef()
        birddFixtureDef.shape = dinoBox
        birddFixtureDef.isSensor = true
        birddFixtureDef.density = 0.1f
        birddFixtureDef.friction = 0f
        body.createFixture(birddFixtureDef)
        birdd.add(body)
        speed += 0

    }


    fun createCactus1() {

        val body = world.createBody(cactiBodyDef)
        body.position.set(100f, 39f)
        val shape = PolygonShape()
        shape.setAsBox(1f, 1f)
        val cactiFixtureDef = FixtureDef()
        cactiFixtureDef.shape = dinoBox
        cactiFixtureDef.isSensor = true
        cactiFixtureDef.density = 0.1f
        cactiFixtureDef.friction = 0f
        body.createFixture(cactiFixtureDef)
        cacti.add(body)
        speed += 20


    }


    fun update(delta: Float, tryJump: Boolean) {
        if (tryJump) {
            tryJump()
        }
        delay -= delta
        bg -= delta

        if (delay < 0) {

            createCactus1()

            delay = random.nextFloat() + rand(2, 3)
        }

        if (bg < -5) {


            createBird1()
            bg = random.nextFloat() + rand(1, 2)
        }
        cacti.forEach {
            it.linearVelocity.set(-delta * speed, 0f)
        }
        cacti.removeIf {
            it.position.x < 0
        }

        birdd.forEach {
            it.linearVelocity.set(-delta * speed, 0f)
        }
        cacti.removeIf {
            it.position.x < 0
        }

        world.step(delta, 4, 3)
        world.setContactListener(this)
    }

    fun tryJump() {
        if (isDinoOnGround()) {
            dino.applyForceToCenter(Vec2(0f, -4250f))
        }
    }

    private fun isDinoOnGround(): Boolean =
            dino.linearVelocity.y == 0.0f

}

