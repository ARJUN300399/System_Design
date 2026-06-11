# Full System Design Notes

These notes are intended as a practical handbook for interviews and real-world architecture work.

---

## 1) What Is System Design?

System design is the process of defining architecture, components, interfaces, and data for a software system so it can meet:

- **Functional requirements** (what it must do)
- **Non-functional requirements (NFRs)** (how well it must do it)

A strong design balances trade-offs: performance, reliability, cost, complexity, and development speed.

---

## 2) Requirement Gathering

Before drawing architecture diagrams, clarify requirements.

### Functional requirements
Examples:
- Users can post, like, and comment.
- Users can search by keyword.
- Payment can be made with card and UPI.

### Non-functional requirements
Examples:
- Read latency < 200ms (p95).
- 99.95% availability.
- Multi-region disaster recovery.
- Data retention for 7 years.

### Constraints
- Budget and team size.
- Time-to-market.
- Compliance (GDPR, HIPAA, PCI-DSS).
- Existing infrastructure.

---

## 3) Back-of-the-Envelope Estimation

Do quick calculations early.

### Traffic and QPS
If daily active users = 10M and each performs 20 requests/day:
- Daily requests = 200M
- Avg QPS = 200M / 86,400 ≈ 2,315
- Peak QPS (x5) ≈ 11,575

### Storage
If each object is 1 KB and 100M objects/day:
- Daily data = ~100 GB/day
- Yearly data ≈ 36.5 TB (before replication/index overhead)

### Bandwidth
Bandwidth = QPS × payload size.
At 10k QPS with 2 KB response:
- ~20 MB/s (~160 Mbps)

These estimates inform database type, cache size, partitioning strategy, and cost.

---

## 4) High-Level Architecture Building Blocks

Typical distributed system components:

1. **Client** (web/mobile)
2. **DNS + CDN** (global routing + static content)
3. **Load balancer** (L4/L7 traffic distribution)
4. **API gateway** (auth, rate limit, routing)
5. **Application services** (business logic)
6. **Datastores** (SQL/NoSQL/object store)
7. **Cache** (Redis/Memcached)
8. **Message queue/stream** (Kafka/RabbitMQ/SQS)
9. **Search engine** (Elasticsearch/OpenSearch)
10. **Monitoring/logging/tracing stack**

---

## 5) Data Storage Choices

## Relational databases (SQL)
Use when you need:
- Strong consistency and transactions (ACID)
- Complex joins
- Well-defined schema

Examples: PostgreSQL, MySQL.

## NoSQL databases
Use when you need:
- Horizontal scalability
- Flexible schema
- Very high throughput

Types:
- Key-value (Redis, DynamoDB)
- Document (MongoDB)
- Wide-column (Cassandra)
- Graph (Neo4j)

## Object storage
For large blobs/media/backups.
Examples: S3, GCS, Azure Blob.

### Rule of thumb
- Start simple with SQL when possible.
- Introduce polyglot persistence only when justified by workload.

---

## 6) CAP and Consistency Models

In distributed systems, under partition you typically choose:
- **CP**: Consistency + Partition tolerance
- **AP**: Availability + Partition tolerance

### Consistency options
- **Strong consistency**: reads always see latest write.
- **Eventual consistency**: data converges over time.
- **Causal/session consistency**: middle-ground guarantees.

Pick per use case:
- Payments/account balances → stronger consistency.
- Social feeds/likes counters → eventual consistency is often acceptable.

---

## 7) API Design Basics

Good API design principles:

- Resource-oriented endpoints.
- Versioning (`/v1/...`) when needed.
- Idempotency for retries (`PUT`, idempotency keys for payments).
- Pagination (`cursor` preferred over offset for large data).
- Standard error schema and status codes.
- Request validation and strict contracts.

Example:

```http
POST /v1/orders
Idempotency-Key: 1fce...
```

---

## 8) Caching Strategy

Caching reduces latency and database load.

### Where to cache
- CDN/edge cache (static content)
- API response cache
- Application object cache
- Database query/result cache

### Patterns
- **Cache-aside (lazy loading)**
- **Write-through**
- **Write-back**
- **Read-through**

### Concerns
- TTL selection
- Cache invalidation
- Thundering herd (use request coalescing, jitter, locks)
- Hot keys and skew

---

## 9) Asynchronous Processing and Queues

Use queues/streams to decouple producers and consumers.

### Benefits
- Smooth traffic spikes
- Improve resilience
- Enable retries and delayed processing

### Key concepts
- At-least-once vs at-most-once vs exactly-once semantics
- Dead-letter queue (DLQ)
- Consumer groups and partitioning
- Idempotent consumers

Use cases:
- Email notifications
- Thumbnail generation
- Analytics pipelines
- Event-driven architecture

---

## 10) Scalability Patterns

### Vertical scaling
Increase machine size (CPU/RAM). Fast but has limits.

### Horizontal scaling
Add more nodes behind load balancers. Better long-term path.

### Database scaling techniques
- Read replicas for read-heavy workloads
- Partitioning/sharding for write scale
- Index tuning and query optimization
- Archival/cold storage for old data

### Sharding considerations
- Shard key choice (uniform distribution)
- Rebalancing strategy
- Cross-shard queries
- Hot partition detection

---

## 11) Reliability and Fault Tolerance

Design for failures as a normal condition.

### Patterns
- Timeouts
- Retries with exponential backoff + jitter
- Circuit breaker
- Bulkheads
- Graceful degradation

### Redundancy
- Multi-AZ deployment
- Multi-region for disaster recovery
- Active-active vs active-passive

### Availability math
If each dependency is 99.9% available, chaining many dependencies lowers end-to-end availability.

---

## 12) Observability

Three pillars:

1. **Metrics** (latency, traffic, errors, saturation)
2. **Logs** (structured, correlated)
3. **Traces** (distributed request flow)

Also define:
- SLI/SLO/SLA
- Alerting thresholds (high signal, low noise)
- Runbooks and incident response playbooks

---

## 13) Security in System Design

Apply security by design:

- Authentication (OIDC/OAuth2/JWT/session)
- Authorization (RBAC/ABAC)
- TLS everywhere
- Encryption at rest
- Secrets management (never hardcode)
- Input validation and output encoding
- Rate limiting and bot protection
- Audit logs and tamper-resistant history

Principles:
- Least privilege
- Defense in depth
- Zero trust mindset

---

## 14) Data Modeling and Indexing

### Schema design
- Keep entities explicit.
- Normalize to reduce duplication, denormalize for read performance when needed.

### Index strategy
- Index high-selectivity query columns.
- Avoid over-indexing (writes become slower).
- Use composite indexes based on query patterns.

### Common anti-patterns
- `SELECT *` on large tables
- Unbounded scans
- N+1 query problems

---

## 15) Common Distributed System Challenges

- Clock skew and ordering issues
- Duplicate event handling
- Partial failures
- Network partitions
- Split-brain scenarios
- Schema migration at scale

Mitigations include idempotency keys, versioned events/schemas, robust retries, and leader-election mechanisms.

---

## 16) Deployment and Release Strategies

- Rolling deployment
- Blue-green deployment
- Canary releases
- Feature flags

Operational best practices:
- Health checks (`liveness`, `readiness`)
- Immutable infrastructure
- Automated rollback
- Infrastructure as code

---

## 17) Design Trade-off Examples

### SQL vs NoSQL
- SQL: consistency + strong transactions
- NoSQL: scale + flexible schema

### Monolith vs Microservices
- Monolith: simpler to start, easier local transactions
- Microservices: independent scaling/deployments but higher operational complexity

### Sync vs Async communication
- Sync (HTTP/RPC): easier request-response flow
- Async (queue/events): better decoupling and resilience

---

## 18) How to Answer a System Design Interview

Recommended structure:

1. Clarify requirements and constraints.
2. Define APIs and core entities.
3. Estimate scale (QPS/storage/bandwidth).
4. Present high-level architecture.
5. Deep dive into bottlenecks and critical components.
6. Discuss reliability, security, and observability.
7. Explain trade-offs and future improvements.

Interview tip: explicitly state assumptions and validate them with the interviewer.

---

## 19) Mini Case Study Templates

### URL Shortener
- Write-heavy key generation service
- Key-value store for short->long mapping
- Cache hot URLs
- Analytics pipeline via async events

### News Feed
- Fan-out on write vs fan-out on read
- Ranking and personalization layer
- Cache timelines
- Background jobs for recomputation

### Chat System
- WebSocket gateways for real-time delivery
- Message store + delivery status
- Offline sync and push notifications
- Presence and typing indicators via pub/sub

---

## 20) Quick Revision Checklist

Before finalizing a design, verify:

- Requirements are explicit and measurable.
- Traffic/storage estimates are realistic.
- Database choices map to access patterns.
- Caching and async processing are justified.
- Failure handling and recovery are covered.
- Security, observability, and operations are included.
- Trade-offs are clearly stated.

---

## 21) Suggested Learning Path

1. Master networking + HTTP + DNS + TCP basics.
2. Learn databases deeply (indexes, transactions, replication).
3. Practice queue/stream-based architectures.
4. Build 3-5 end-to-end design case studies.
5. Review postmortems from real production incidents.

Consistent practice is the fastest path to strong system design skills.
