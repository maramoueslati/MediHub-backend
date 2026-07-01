const Notification = require('../models/Notification');
const { sendEmail } = require('../config/mailer');
const { getEmailTemplate } = require('../config/emailTemplates');

const startConsumer = async (channel) => {
    if (!channel) {
        console.error('❌ Canal RabbitMQ non disponible');
        return;
    }

    const queue = process.env.RABBITMQ_QUEUE;
    channel.prefetch(1);
    console.log(`👂 En attente de messages sur la queue: ${queue}`);

    channel.consume(queue, async (msg) => {
        if (!msg) return;
        try {
            const data = JSON.parse(msg.content.toString());
            console.log('📨 Message reçu:', data);

            const message = buildMessage(data);

            // Save to MongoDB
            const notification = new Notification({
                rendezVousId: data.rendezVousId,
                patientId: data.patientId,
                medecinId: data.medecinId,
                type: data.type,
                message: message,
                dateRendezVous: data.dateRendezVous,
                heureRendezVous: data.heureRendezVous,
                motif: data.motif
            });

            await notification.save();
            console.log(`✅ Notification sauvegardée: [${data.type}] RDV #${data.rendezVousId}`);

            // Send email if patient email is provided
            if (data.patientEmail) {
                const template = getEmailTemplate(data.type, data);
                await sendEmail(data.patientEmail, template.subject, template.html);
            }

            channel.ack(msg);

        } catch (error) {
            console.error('❌ Erreur:', error.message);
            channel.nack(msg, false, true);
        }
    });
};

const buildMessage = (data) => {
    const date = data.dateRendezVous || 'N/A';
    const heure = data.heureRendezVous || 'N/A';
    const motif = data.motif || 'Consultation';

    switch (data.type) {
        case 'RDV_CREE':
            return `Votre rendez-vous du ${date} à ${heure} a été créé avec succès. Motif: ${motif}`;
        case 'RDV_CONFIRME':
            return `Votre rendez-vous du ${date} à ${heure} a été confirmé par le médecin.`;
        case 'RDV_ANNULE':
            return `Votre rendez-vous du ${date} à ${heure} a été annulé.`;
        case 'RDV_REPORTE':
            return `Votre rendez-vous du ${date} à ${heure} a été reporté.`;
        default:
            return `Mise à jour de votre rendez-vous du ${date} à ${heure}.`;
    }
};

module.exports = { startConsumer };