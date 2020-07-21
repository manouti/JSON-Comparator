# JSON-Comparator

Compares two JSON documents and outputs differences. Fields can be excluded:

##Left document

```
{
    "name": "john", 
    "score": 5.1,
    "toto": {
       "tata": 3.1
    }
}
```

##Right document

```
{
    "name": "jane", 
    "score": 5,
    "toto": {
       "x": 3,
       "tata": 3
    }
}
```


```
[
     { "type": "numeric", "path": "/score", "tolerance": 0.2 },
     { "type": "exclude", "path": "/name" },
     { "type": "exclude", "path": "/toto/x" }
]
```

##Output

```
Entries only on left:

Entries only on right:

Entries differing:
/toto/tata=(3.1, 3)
Ignored entries only on left:

Ignored entries only on right:
/toto/x

Tolerated differences:
/score=(5.1, 5)
/name=(john, jane)
```
