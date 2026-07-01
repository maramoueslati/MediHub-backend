const express = require('express');
const router = express.Router();
const Notification = require('../models/Notification');

// GET all notifications
router.get('/', async (req, res) => {
    try {
        const notifications = await Notification.find().sort({ createdAt: -1 });
        res.json(notifications);
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
});

// GET notifications by patientId
router.get('/patient/:patientId', async (req, res) => {
    try {
        const notifications = await Notification.find({
            patientId: req.params.patientId
        }).sort({ createdAt: -1 });
        res.json(notifications);
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
});

// GET notifications by medecinId
router.get('/medecin/:medecinId', async (req, res) => {
    try {
        const notifications = await Notification.find({
            medecinId: req.params.medecinId
        }).sort({ createdAt: -1 });
        res.json(notifications);
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
});

// GET notifications by rendezVousId
router.get('/rdv/:rendezVousId', async (req, res) => {
    try {
        const notifications = await Notification.find({
            rendezVousId: req.params.rendezVousId
        }).sort({ createdAt: -1 });
        res.json(notifications);
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
});

// GET unread notifications by patientId
router.get('/patient/:patientId/non-lues', async (req, res) => {
    try {
        const notifications = await Notification.find({
            patientId: req.params.patientId,
            lu: false
        }).sort({ createdAt: -1 });
        res.json(notifications);
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
});

// PATCH mark notification as read
router.patch('/:id/lire', async (req, res) => {
    try {
        const notification = await Notification.findByIdAndUpdate(
            req.params.id,
            { lu: true },
            { new: true }
        );
        if (!notification) return res.status(404).json({ message: 'Notification introuvable' });
        res.json(notification);
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
});

// PATCH mark all notifications as read for a patient
router.patch('/patient/:patientId/tout-lire', async (req, res) => {
    try {
        await Notification.updateMany(
            { patientId: req.params.patientId, lu: false },
            { lu: true }
        );
        res.json({ message: 'Toutes les notifications marquées comme lues' });
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
});

// DELETE notification by id
router.delete('/:id', async (req, res) => {
    try {
        await Notification.findByIdAndDelete(req.params.id);
        res.json({ message: 'Notification supprimée' });
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
});

// GET health check
router.get('/ping', (req, res) => {
    res.json({ status: 'UP', service: 'notification-service', port: 3000 });
});

module.exports = router;
