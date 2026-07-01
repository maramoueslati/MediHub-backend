const nodemailer = require('nodemailer');

const transporter = nodemailer.createTransport({
    service: 'gmail',
    auth: {
        user: process.env.EMAIL_USER,
        pass: process.env.EMAIL_PASSWORD
    }
});

const sendEmail = async (to, subject, html) => {
    try {
        const info = await transporter.sendMail({
            from: process.env.EMAIL_FROM,
            to: to,
            subject: subject,
            html: html
        });
        console.log(`📧 Email envoyé à ${to}: ${info.messageId}`);
        return true;
    } catch (error) {
        console.error(`❌ Erreur envoi email à ${to}:`, error.message);
        return false;
    }
};

module.exports = { sendEmail };