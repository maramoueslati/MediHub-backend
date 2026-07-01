const amqp = require('amqplib');

let connection = null;
let channel = null;

const connectRabbitMQ = async () => {
    try {
        connection = await amqp.connect(process.env.RABBITMQ_URL);
        channel = await connection.createChannel();

        // Declare the queue (durable = survives broker restart)
        await channel.assertQueue(process.env.RABBITMQ_QUEUE, { durable: true });

        console.log('✅ RabbitMQ connecté:', process.env.RABBITMQ_URL);
        console.log('📬 Queue:', process.env.RABBITMQ_QUEUE);

        return channel;
    } catch (error) {
        console.error('❌ Erreur RabbitMQ:', error.message);
        console.log('⏳ Retry dans 5 secondes...');
        setTimeout(connectRabbitMQ, 5000);
    }
};

const getChannel = () => channel;

module.exports = { connectRabbitMQ, getChannel };
