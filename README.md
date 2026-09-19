# Phantom 

### Cyber Deception System

A honeypot-based intrusion deception system built in Java.
Phantom lures attackers into a fake environment, logs their every move,
and tracks dangerous IPs — all while the real service stays hidden.

---

## How it works

When a client connects to Phantom:
- **Legitimate paths** → served normally
- **Suspicious paths** → routed to a fake service with convincing responses
- **Every suspicious request** → logged with IP, path, timestamp, and user-agent
- **Repeated attackers** → flagged as DANGER and permanently tracked

The attacker never knows they're being watched.

---

## Architecture

- Gateway → accepts all incoming connections
- ClientHandler → handles each client in a separate thread
- HttpRequest → parses raw HTTP requests
- Router → decides real or fake response
- FakeService → returns convincing fake responses
- ThreatTracker → tracks suspicious IPs (persistent across restarts)
- Logger → logs to console and file
- HttpResponse → builds HTTP responses


---

## Getting started

**Requirements:**
- Java 17+
- No external dependencies

**Run:**
```bash
# Clone the repo
git clone https://github.com/yourusername/phantom.git
cd phantom

# Compile
javac -d out src/org/phantom/*.java

# Run
java -cp out org.phantom.Gateway
```

**Test:**
```bash
# Normal request
curl http://localhost:8080/api/users

# Suspicious request (will be logged)
curl http://localhost:8080/admin
curl http://localhost:8080/.env
```

---

## Logs

All suspicious activity is logged to:
- **Console** — real-time monitoring
- **`phantom.log`** — persistent log file
- **`danger_ips.txt`** — permanently banned IPs (survive restarts)

Example log output:
```bash
[2026-09-19 15:01:42] [SUSPICIOUS] IP: /127.0.0.1 | Path: /admin | Agent: curl/8.21.0
[2026-09-19 15:02:10] [DANGER - Scanning detected! Count: 3] IP: /127.0.0.1 | Path: /.env | Agent: curl/8.21.0
```

---

## Concepts

This project is inspired by real-world cyber deception techniques:

- **Honeypot** — a fake service that attracts attackers
- **Honeytoken** — fake credentials and data that trigger alerts when accessed
- **Deception Fabric** — multiple fake services that mirror the real system
- **Observe, don't block** — let attackers think they're winning while we learn from them

---

p.s : this is a learning project in Java networking and cybersecurity concepts.
it't not fully safe... but it's good for practice