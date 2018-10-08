
A simple REST server. It uses an atom to store the data in memory, although
SQLite/Datomic options would work.

Some of its endpoints are protected with `clojure.spec` to prevent incorrect
input.

Usage:

```bash
lein run
```

Then point your browser to [localhost:8080](http://localhost:8080).
