\# 🏙️ AI-Driven Smart City Grievance Redressal System



A centralized, intelligent, and transparent web platform for citizens to report civic issues and for municipal corporations to resolve them efficiently using Artificial Intelligence.



> \*\*B.Tech CSE — Semester VII — Project Phase I\*\*  

> \*\*Group ID:\*\* CSE-C-15



\---



\## 📌 Problem Statement



Municipal corporations receive a large number of citizen complaints related to public services, but existing grievance systems are often manual, fragmented, and lack proper tracking. Complaints are delayed, poorly managed, and citizens have limited visibility into their resolution status.



There is no centralized platform to efficiently monitor, assign, and track complaints across departments. An AI-driven centralized grievance redressal system is needed to improve complaint management, transparency, and service delivery.



\---



\## 🎯 Objectives



\- Develop a centralized platform for citizens to report and track civic complaints.

\- Use AI to automatically classify, prioritize, and route complaints to the correct department.

\- Provide real-time status updates and notifications to citizens.

\- Reduce complaint resolution time and enhance municipal service efficiency.

\- Enable administrators with analytics dashboards for data-driven decisions.



\---



\## ✨ Key Features



\### 👤 Citizen

\- Register/login with OTP

\- Submit complaints with image, description, and GPS location

\- Track complaint status in real time

\- Receive notifications (SMS / Push / WhatsApp)

\- Rate and reopen resolved complaints



\### 🧑‍💼 Department Manager

\- View AI-triaged complaints

\- Create work orders and assign field workers

\- Monitor SLA and escalation

\- Department-level analytics



\### 👷 Field Worker

\- Receive task assignments

\- Upload geo-tagged before/after resolution photos

\- Update status on the go



\### 🛡️ Admin

\- Manage users, roles, departments, wards

\- View city-wide analytics and heatmaps

\- Generate reports (PDF, CSV)

\- Audit logs and RTI-ready exports



\### 🤖 AI Capabilities

\- Image classification (pothole, garbage, streetlight, waterlogging, etc.)

\- Text classification and priority prediction

\- Automatic department routing

\- Duplicate complaint detection

\- Predictive hotspot detection (future phase)



\---



\## 🛠️ Tech Stack



| Layer | Technology |

|-------|-----------|

| Frontend | React.js, Tailwind CSS, React Router, Axios |

| Backend | Java 21, Spring Boot, Spring Data JPA, Spring Security |

| AI Service | Python, FastAPI, TensorFlow / PyTorch |

| Database | MySQL (production), H2 (development) |

| Image Storage | Cloudinary / AWS S3 |

| Maps | Google Maps Platform |

| Auth | JWT + BCrypt |

| DevOps | Docker, GitHub Actions, Maven |



\---



\## 📁 Project Structure



```

smart-city-grievance-system/

├── backend/          # Spring Boot backend

├── frontend/         # React frontend

├── ai-service/       # Python AI microservice

├── docs/             # Diagrams, design docs

└── README.md

```



\---



\## 🚀 Getting Started



\### Prerequisites

\- Java 21+

\- Maven 3.9+

\- Node.js 20+ and npm

\- MySQL 8 (for production)

\- Git



\### Backend Setup

```bash

cd backend/grievance-backend

./mvnw spring-boot:run

```



Backend runs at: `http://localhost:8080`



\### Frontend Setup

```bash

cd frontend

npm install

npm run dev

```



\---



\## 👥 Team



| PRN | Name |

|-----|------|

| 230105131278 | Kalpesh Wagh |

| 230105131279 | Pawan Wagh |

| 230105131336 | Aditya Khakare |

| 230105131339 | Koyalkar Rithish |



\*\*Project Guide:\*\* Mr. Pratik Bodke



\---



\## 📅 Roadmap



\- \[x] Phase 1 — Requirement analysis, system design, architecture

\- \[ ] Phase 2 — Backend: auth, users, complaints, work orders

\- \[ ] Phase 3 — Frontend: dashboards

\- \[ ] Phase 4 — AI service integration

\- \[ ] Phase 5 — Notifications, analytics, deployment

\- \[ ] Phase 6 — Testing, documentation, final demo



\---



\## 📄 License



Developed for academic purposes as part of B.Tech CSE Semester VII.

