const { Kafka, logLevel } = require("kafkajs");
const isDocker = require('is-docker');

if (isDocker()) {
    console.log("Running in docker")
} else {
    console.log("Running locally")
}

const brokers = isDocker() ? ["kafka:9093"] : ["localhost:9092"]
const topic = "Topic1"
const clientId = "kafka-consumer"

const kafka = new Kafka({
    logLevel: logLevel.INFO,
    brokers,
    clientId
})

const consumer = kafka.consumer({ groupId: 'group' })

const run = async () => {
    await consumer.connect()
    await consumer.subscribe({ topic, fromBeginning: true })
    await consumer.run({
        eachMessage: async ({ topic, partition, message }) => {
            const prefix = `${topic}[${partition} | ${message.offset}]`
            console.log(`${prefix} - ${message.value}`)
        },
    })
}

run().catch(e => console.error(`[example/consumer] ${e.message}`, e))

const errorTypes = ['unhandledRejection', 'uncaughtException']
const signalTraps = ['SIGTERM', 'SIGINT', 'SIGUSR2']

errorTypes.map(type => {
    process.on(type, async e => {
        try {
            console.log(`process.on ${type}`)
            console.error(e)
            await consumer.disconnect()
            process.exit(0)
        } catch (_) {
            process.exit(1)
        }
    })
})

signalTraps.map(type => {
    process.once(type, async () => {
        try {
            await consumer.disconnect()
        } finally {
            process.kill(process.pid, type)
        }
    })
})