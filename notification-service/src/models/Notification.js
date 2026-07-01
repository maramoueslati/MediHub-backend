const mongoose = require('mongoose');

const notificationSchema = new mongoose.Schema({
    // Reference to the rendez-vous
    rendezVousId: {
        type: Number,
        required: true
    },

    // Who is concerned
    patientId: {
        type: Number,
        required: true
    },
    medecinId: {
        type: Number,
        required: true
    },

    // Notification details
    type: {
        type: String,
        enum: ['RDV_CREE', 'RDV_CONFIRME', 'RDV_ANNULE', 'RDV_REPORTE'],
        required: true
    },

    message: {
        type: String,
        required: true
    },

    // RDV details
    dateRendezVous: String,
    heureRendezVous: String,
    motif: String,

    // Notification status
    lu: {
        type: Boolean,
        default: false
    },

    // Timestamps
    createdAt: {
        type: Date,
        default: Date.now
    }
});

module.exports = mongoose.model('Notification', notificationSchema);
