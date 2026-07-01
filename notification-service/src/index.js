require('dotenv').config();
const express = require('express');
const cors = require('cors');
const connectDB = require('./config/database');
const { connectRabbitMQ } = require('./config/rabbitmq');
const { startConsumer } = require('./consumers/rdvConsumer');
const notificationRoutes = require('./routes/notifications');

const app = express();
const PORT = process.env.PORT || 3000;

// Middleware
app.use(cors());
app.use(express.json());

// Routes
app.use('/api/notifications', notificationRoutes);

// Root
app.get('/', (req, res) => {
    res.json({
        service: 'MediHub Notification Service',
        status: 'UP',
        port: PORT,
        endpoints: [
            'GET  /api/notifications',
            'GET  /api/notifications/patient/:patientId',
            'GET  /api/notifications/medecin/:medecinId',
            'GET  /api/notifications/rdv/:rendezVousId',
            'GET  /api/notifications/patient/:patientId/non-lues',
            'PATCH /api/notifications/:id/lire',
            'PATCH /api/notifications/patient/:patientId/tout-lire',
            'DELETE /api/notifications/:id'
        ]
    });
});

// Start server
const startServer = async () => {
    // Connect MongoDB
    await connectDB();

    // Connect RabbitMQ and start consumer
    const channel = await connectRabbitMQ();
    if (channel) {
        await startConsumer(channel);
    }

    // Start Express server
    app.listen(PORT, () => {
        console.log(`🚀 Notification Service démarré sur le port ${PORT}`);
        console.log(`📡 API: http://localhost:${PORT}/api/notifications`);
    });
};

startServer();
