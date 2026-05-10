# 🚨 Emergency Help App (SOS)

### Android | Java/Kotlin | Real-time Safety & GPS Alerts

A robust personal safety application designed to provide immediate assistance during emergencies. With a single tap, the app triggers automated alerts, shares real-time location data, and connects users to emergency services.

---

## 📸 Screenshots



| SOS Dashboard | Real-time Tracking | Emergency Contacts |
| --- | --- | --- |
|  |  |  |

---

## 🚀 Key Features

* **Instant SOS Trigger:** One-tap activation to send alerts to pre-defined emergency contacts.
* **GPS Integration:** Automatically fetches high-accuracy coordinates using Google Fused Location Provider.
* **Automated SMS Alerts:** Sends emergency messages with a Google Maps location link even if the user cannot speak.
* **Background Protection:** Runs as a foreground service to ensure the app stays active during critical moments.
* **Hardware Trigger:** (If applicable) Support for triggering SOS via volume buttons or power button double-tap.

---

## 🛠️ Tech Stack & Architecture

* **Language:** Java / Kotlin
* **Android Architecture:** MVVM (Model-View-ViewModel) for clean code separation.
* **APIs & Libraries:**
* **Google Play Services Location:** For precise tracking.
* **Room Database:** For local storage of emergency contacts and user profiles.
* **SmsManager:** For automated alert dispatching.


* **Permissions:** Implements strict runtime permission handling for Location, SMS, and Contacts.

---

## ⚙️ Setup & Installation

1. **Clone the Repo:**
```bash
git clone https://github.com/KeshavGhosal/emergency-help-app.git

```


2. **Open in Android Studio:** Import the project and let Gradle sync.
3. **API Keys:** * Obtain a **Google Maps API Key** from the Google Cloud Console.
* Add your key to `local.properties`: `MAPS_API_KEY=YOUR_KEY_HERE`.


4. **Run:** Build and deploy to an emulator or physical Android device (API Level 24+ recommended).

---

## 🛡️ Security & Privacy

* **Local Processing:** All emergency contact data is stored locally on the device using encrypted Room database entries.
* **Minimal Permissions:** The app only requests permissions essential for emergency functionality.

---

## 🤝 Contribution

Contributions are welcome! If you'd like to improve the SOS algorithms or UI, please fork the repo and submit a PR.

---
