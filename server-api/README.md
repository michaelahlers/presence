# Michael Ahlers Presence API Server

## Development

### Deployment

#### Heroku

Deploy to a specific Heroku environment with:

```shell
./activator -Denv=ENVIRONMENT ';stage;deployHeroku'
```

Where _ENVIRONMENT_ is among: `qa`, or `prod`.
