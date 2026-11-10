// Seeds Firestore "products" collection from products.json.
// Requires: npm install firebase-admin
// Usage: node seed.js
//
// You need a service account key from Firebase Console:
//   Project Settings -> Service Accounts -> Generate new private key
// Save it as serviceAccountKey.json in this same folder.

const admin = require("firebase-admin");
const products = require("./products.json");
const serviceAccount = require("./serviceAccountKey.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
});

const db = admin.firestore();

async function seed() {
  const batch = db.batch();
  products.forEach((product) => {
    const docRef = db.collection("products").doc();
    batch.set(docRef, product);
  });
  await batch.commit();
  console.log(`Seeded ${products.length} products into Firestore.`);
}

seed()
  .then(() => process.exit(0))
  .catch((err) => {
    console.error("Seeding failed:", err);
    process.exit(1);
  });
