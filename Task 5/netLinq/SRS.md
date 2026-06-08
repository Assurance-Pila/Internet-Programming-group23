# NetLinq — Software Requirement Specification

**QoE Mobile Network Monitoring Application**

> Reference document for the NetLinq Android project. Keep this file updated as requirements evolve.

---

## Table of Contents

1. [Introduction](#1-introduction)
2. [Overall Description](#2-overall-description)
3. [System Architecture](#3-system-architecture)
4. [Functional Requirements](#4-functional-requirements)
5. [Non-Functional Requirements](#5-non-functional-requirements)
6. [External Interface Requirements](#6-external-interface-requirements)
7. [Data Management Requirements](#7-data-management-requirements)
8. [Security and Privacy Requirements](#8-security-and-privacy-requirements)
9. [System Constraints](#9-system-constraints)
10. [Future Enhancements](#10-future-enhancements)
11. [Implementation Decisions](#11-implementation-decisions)

---

## 1. Introduction

### 1.1 Purpose of the System

The purpose of this system is to develop a native Android mobile application capable of collecting both subjective Quality of Experience (QoE) feedback and objective network performance indicators from mobile subscribers in Cameroon.

The system is designed to bridge the gap between traditional network-side Quality of Service (QoS) monitoring and actual user-perceived mobile network experience.

Existing telecom monitoring systems provide operators with technical infrastructure data but do not adequately capture real-time user experience, contextual network issues, and subscriber perception. This system addresses that limitation by combining automated background network monitoring with event-triggered user feedback collection.

### 1.2 Problem Statement

Mobile network users in Cameroon heavily rely on mobile internet for communication, education, work, streaming, and social interaction. However, users continue to experience poor network quality including:

- Slow internet speed
- Signal instability
- Network outages
- High latency
- Video buffering
- Poor call quality

Despite telecom providers possessing strong QoS monitoring systems, there remains limited visibility into actual subscriber Quality of Experience.

The proposed system aims to provide a lightweight, privacy-conscious, and scalable QoE monitoring platform capable of collecting contextual user-centered network experience data.

### 1.3 Scope of the System

The system focuses on Android smartphones and supports:

- Background network monitoring
- Subjective user feedback collection
- Signal strength monitoring
- Network type detection
- Latency measurement
- Offline-first data storage
- Cloud synchronization
- User QoE dashboard
- Aggregated operator reports

The system does **not** attempt to access privileged telecom infrastructure metrics or operator internal QoS systems.

### 1.4 Target Users

| User Type | Description |
|-----------|-------------|
| **Subscribers** | Mobile users who install the application to monitor their personal network experience and contribute QoE data. |
| **Network Operators** | Telecom operators such as MTN and Orange who use anonymized aggregated reports to understand subscriber experience patterns. |
| **System Administrators** | Developers and backend administrators responsible for maintaining the infrastructure and ensuring data integrity. |

---

## 2. Overall Description

### 2.1 Product Perspective

The application is a standalone native Android system that continuously collects lightweight contextual network indicators and user experience feedback.

The system follows an **Offline-First** architecture where all collected data is stored locally first before synchronization with the backend cloud infrastructure.

The application combines:

- Passive objective monitoring
- Active subjective feedback
- Local analytics
- Cloud-based aggregation

### 2.2 Product Functions

The system shall:

- Collect subjective QoE ratings from users
- Collect objective network indicators from the device
- Detect network degradation events
- Trigger contextual feedback prompts
- Store collected data locally
- Synchronize data to cloud servers
- Display personal QoE history to users
- Generate anonymized operator analytics
- Operate efficiently with minimal battery and data usage

### 2.3 Operating Environment

| Component | Specification |
|-----------|---------------|
| Platform | Android |
| Minimum Android Version | Android 8.0 (Oreo) — API 26 |
| Programming Language | Kotlin |
| UI Framework | Jetpack Compose |
| Backend | Supabase |
| Network Connectivity | Mobile Data / WiFi |
| Local Database | Room Database |

### 2.4 Design Constraints

The system is constrained by:

- Android background execution restrictions
- Permission limitations on newer Android versions
- Battery optimization policies
- User privacy concerns
- Device manufacturer differences
- Internet connectivity availability

### 2.5 Assumptions and Dependencies

The system assumes:

- Users grant required permissions
- Users possess Android smartphones
- Internet connectivity becomes periodically available for synchronization
- Users allow notification access
- Backend services remain operational

---

## 3. System Architecture

### 3.1 Architectural Pattern

The application adopts **MVVM** (Model-View-ViewModel) combined with **Clean Architecture** principles.

Benefits:

- Separation of concerns
- Lifecycle awareness
- Scalability
- Improved maintainability
- Better background task management
- Easier testing and debugging

### 3.2 Architectural Layers

#### Presentation Layer

Responsible for: user interfaces, user interaction, permission requests, dashboard visualization, notification handling.

Technologies: Jetpack Compose, Android Activities, ViewModels.

#### Domain Layer

Responsible for: business logic, QoE analysis, event detection, feedback trigger logic, network quality evaluation.

#### Data Layer

Responsible for: local database access, API communication, data synchronization, network metric collection, repository management.

Technologies: Room Database, Retrofit/Ktor, Supabase APIs.

### 3.3 Monitoring Architecture

The application uses intelligent context-aware monitoring instead of unrestricted continuous monitoring.

Monitoring occurs during:

- Active device usage
- Screen-on periods
- Connectivity changes
- Detected degradation events
- Periodic lightweight background checks

This reduces battery consumption, background restrictions, user distrust, and data usage.

### 3.4 Android APIs Used

| API | Purpose |
|-----|---------|
| TelephonyManager | Signal strength and network type |
| ConnectivityManager | Connectivity monitoring |
| WorkManager | Scheduled background tasks |
| NotificationManager | Feedback prompts |
| Room Database | Local storage |
| Retrofit | Backend synchronization |
| PowerManager | Device activity awareness |

---

## 4. Functional Requirements

| ID | Requirement |
|----|-------------|
| **FR1** | Generate a unique anonymous device identifier for each installation. |
| **FR2** | Allow users to submit subjective QoE ratings: overall experience, internet responsiveness, streaming experience, call quality, general satisfaction. |
| **FR3** | Trigger feedback prompts on signal degradation, network type changes, latency spikes, connectivity interruptions. Users can configure feedback frequency. |
| **FR4** | Automatically collect signal strength, signal quality indicators, and cellular network status via TelephonyManager. |
| **FR5** | Detect 2G, 3G, 4G, 5G, and WiFi network connections. |
| **FR6** | Periodically measure network latency using lightweight network requests. |
| **FR7** | Store all collected records locally using Room Database before synchronization. |
| **FR8** | Continue operating without internet connectivity. |
| **FR9** | Synchronize unsynced records to Supabase when connectivity is available. |
| **FR10** | Display personal QoE history, network trend charts, signal quality trends, and usage summaries. |
| **FR11** | Generate anonymized aggregated operator reports: average QoE scores, degradation patterns, network distribution, complaint trends. |
| **FR12** | Automatically remove synchronized records older than 7 days from local storage. |

---

## 5. Non-Functional Requirements

### 5.1 Performance

- Low CPU and memory usage
- Dashboard screens load within 3 seconds under normal conditions
- Background monitoring tasks remain lightweight

### 5.2 Battery Efficiency

- Event-driven collection prioritized
- Continuous aggressive monitoring avoided
- Synchronization defaults to WiFi-only mode

### 5.3 Privacy

- No personally identifiable information uploaded
- Data anonymized before synchronization
- Users control participation

### 5.4 Security

- All communication over HTTPS
- Authenticated backend APIs
- Local sensitive records encrypted
- Supabase Row-Level Security enabled

### 5.5 Reliability

- Offline operation supported
- Unsynchronized data persists until upload succeeds
- Background monitoring recovers after device reboot

### 5.6 Scalability

- Backend supports increasing user data volume across multiple regions

### 5.7 Usability

- Feedback prompts completable in under 30 seconds
- Simple, non-intrusive UI
- Easy-to-understand dashboard visualizations

---

## 6. External Interface Requirements

### 6.1 User Interface

- Login/setup screens
- Permission request screens
- Dashboard screens
- Feedback forms
- Settings screens
- Notification prompts

### 6.2 Hardware Interface

Interacts with cellular modem hardware, device network interfaces, and local storage through Android system APIs.

### 6.3 Software Interface

| Component | Interface |
|-----------|-----------|
| Android OS | Android SDK APIs |
| Backend | REST APIs (Supabase) |
| Database | Room ORM |
| Authentication | Anonymous device ID (v1) |
| Analytics | Supabase APIs |

### 6.4 Communication

- HTTPS
- JSON payloads
- RESTful APIs

---

## 7. Data Management Requirements

### 7.1 Data Collected

- QoE ratings
- Signal strength
- Network type
- Latency measurements
- Device model
- Android version
- Timestamps

### 7.2 Storage Strategy (Offline-First)

1. Data collected locally
2. Records marked unsynced
3. Synchronization when internet available
4. Records marked synced after successful upload
5. Old synchronized records purged automatically

### 7.3 Data Retention

- Unsynced records remain until upload succeeds
- Synced records older than 7 days deleted locally
- Aggregated backend analytics may remain long-term

---

## 8. Security and Privacy Requirements

### 8.1 User Consent

Request consent before: network monitoring, notification access, data synchronization, permission activation.

### 8.2 Permissions

| Permission | Purpose |
|------------|---------|
| INTERNET | Network communication |
| ACCESS_NETWORK_STATE | Connectivity detection |
| READ_PHONE_STATE | Telephony information |
| POST_NOTIFICATIONS | Feedback notifications |
| ACCESS_FINE_LOCATION | Signal and cell information |

### 8.3 Privacy Protection

- Avoid collecting personal identifiers
- Anonymize uploaded records
- Provide transparency to users
- Allow users to stop monitoring at any time

---

## 9. System Constraints

The system cannot access:

- Telecom core network data
- Privileged carrier APIs
- Operator infrastructure systems

Additional constraints: battery optimization policies, OEM process restrictions, permission sensitivity, hardware differences.

---

## 10. Future Enhancements

- AI-based QoE prediction
- Heatmap visualization
- Packet loss monitoring
- Jitter analysis
- Cross-platform support
- Advanced operator analytics
- Predictive congestion detection

---

## 11. Implementation Decisions

Decisions made during project setup (may differ from original draft spec):

| Topic | Decision | Rationale |
|-------|----------|-----------|
| **Auth** | Anonymous device UUID (v1) | Matches FR1 and privacy requirements; no login friction |
| **Backend** | Supabase | As specified; PostgreSQL + REST + RLS |
| **Sync default** | WiFi-only (user can override) | Battery and data efficiency |
| **Package name** | `com.netlinq` | Project branding |

---

## Conclusion

NetLinq provides a technically feasible solution for collecting real-time user-centered network experience data in Cameroon. The combination of subjective QoE feedback with objective device-level indicators, offline-first sync, event-driven monitoring, and anonymized analytics forms a strong foundation for implementation.
