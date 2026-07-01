const getEmailTemplate = (type, data) => {
    const date = data.dateRendezVous || 'N/A';
    const heure = data.heureRendezVous || 'N/A';
    const motif = data.motif || 'Consultation';

    const templates = {
        RDV_CREE: {
            subject: '✅ Rendez-vous créé - MediHub',
            html: `
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                    <div style="background: #a8d5d1; padding: 20px; text-align: center;">
                        <h1 style="color: #2c4a52; margin: 0;">💊 MediHub</h1>
                    </div>
                    <div style="padding: 30px; background: #f9fdfd;">
                        <h2 style="color: #2c4a52;">Rendez-vous créé avec succès ✅</h2>
                        <p>Votre rendez-vous a été enregistré dans notre système.</p>
                        <div style="background: white; border-left: 4px solid #a8d5d1; padding: 15px; margin: 20px 0;">
                            <p><strong>📅 Date:</strong> ${date}</p>
                            <p><strong>⏰ Heure:</strong> ${heure}</p>
                            <p><strong>📋 Motif:</strong> ${motif}</p>
                            <p><strong>🔖 Statut:</strong> En attente de confirmation</p>
                        </div>
                        <p style="color: #6b8f95;">En attente de confirmation par le médecin.</p>
                    </div>
                    <div style="background: #2c4a52; padding: 15px; text-align: center; color: white;">
                        <p style="margin: 0;">© 2026 MediHub - Plateforme Médicale</p>
                    </div>
                </div>
            `
        },
        RDV_CONFIRME: {
            subject: '🎉 Rendez-vous confirmé - MediHub',
            html: `
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                    <div style="background: #a8d5d1; padding: 20px; text-align: center;">
                        <h1 style="color: #2c4a52; margin: 0;">💊 MediHub</h1>
                    </div>
                    <div style="padding: 30px; background: #f9fdfd;">
                        <h2 style="color: #27ae60;">Rendez-vous confirmé 🎉</h2>
                        <p>Votre médecin a confirmé votre rendez-vous.</p>
                        <div style="background: white; border-left: 4px solid #27ae60; padding: 15px; margin: 20px 0;">
                            <p><strong>📅 Date:</strong> ${date}</p>
                            <p><strong>⏰ Heure:</strong> ${heure}</p>
                            <p><strong>📋 Motif:</strong> ${motif}</p>
                            <p><strong>🔖 Statut:</strong> <span style="color: #27ae60;">CONFIRMÉ</span></p>
                        </div>
                        <p style="color: #6b8f95;">Merci de vous présenter à l'heure indiquée.</p>
                    </div>
                    <div style="background: #2c4a52; padding: 15px; text-align: center; color: white;">
                        <p style="margin: 0;">© 2026 MediHub - Plateforme Médicale</p>
                    </div>
                </div>
            `
        },
        RDV_ANNULE: {
            subject: '❌ Rendez-vous annulé - MediHub',
            html: `
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                    <div style="background: #a8d5d1; padding: 20px; text-align: center;">
                        <h1 style="color: #2c4a52; margin: 0;">💊 MediHub</h1>
                    </div>
                    <div style="padding: 30px; background: #f9fdfd;">
                        <h2 style="color: #e74c3c;">Rendez-vous annulé ❌</h2>
                        <p>Votre rendez-vous a été annulé.</p>
                        <div style="background: white; border-left: 4px solid #e74c3c; padding: 15px; margin: 20px 0;">
                            <p><strong>📅 Date:</strong> ${date}</p>
                            <p><strong>⏰ Heure:</strong> ${heure}</p>
                            <p><strong>📋 Motif:</strong> ${motif}</p>
                            <p><strong>🔖 Statut:</strong> <span style="color: #e74c3c;">ANNULÉ</span></p>
                        </div>
                        <p style="color: #6b8f95;">Vous pouvez prendre un nouveau rendez-vous sur MediHub.</p>
                    </div>
                    <div style="background: #2c4a52; padding: 15px; text-align: center; color: white;">
                        <p style="margin: 0;">© 2026 MediHub - Plateforme Médicale</p>
                    </div>
                </div>
            `
        },
        RDV_REPORTE: {
            subject: '🔄 Rendez-vous reporté - MediHub',
            html: `
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                    <div style="background: #a8d5d1; padding: 20px; text-align: center;">
                        <h1 style="color: #2c4a52; margin: 0;">💊 MediHub</h1>
                    </div>
                    <div style="padding: 30px; background: #f9fdfd;">
                        <h2 style="color: #8e44ad;">Rendez-vous reporté 🔄</h2>
                        <p>Votre rendez-vous a été reporté.</p>
                        <div style="background: white; border-left: 4px solid #8e44ad; padding: 15px; margin: 20px 0;">
                            <p><strong>📅 Date initiale:</strong> ${date}</p>
                            <p><strong>⏰ Heure initiale:</strong> ${heure}</p>
                            <p><strong>📋 Motif:</strong> ${motif}</p>
                            <p><strong>🔖 Statut:</strong> <span style="color: #8e44ad;">REPORTÉ</span></p>
                        </div>
                        <p style="color: #6b8f95;">Veuillez reprendre rendez-vous sur MediHub.</p>
                    </div>
                    <div style="background: #2c4a52; padding: 15px; text-align: center; color: white;">
                        <p style="margin: 0;">© 2026 MediHub - Plateforme Médicale</p>
                    </div>
                </div>
            `
        }
    };

    return templates[type] || {
        subject: 'Mise à jour rendez-vous - MediHub',
        html: `<p>Mise à jour de votre rendez-vous du ${date} à ${heure}.</p>`
    };
};

module.exports = { getEmailTemplate };