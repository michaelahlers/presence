# Michael Ahlers Presence: Browser Client

## Development

### Setup

1. [Install Node.js](https://github.com/michaelahlers/michaelahlers-presence/wiki/Development#nodejs).
1. [Install Amazon Web Service CLI](https://github.com/michaelahlers/michaelahlers-presence/wiki/Development#amazon-web-service-cli).
   - Optional; only for QA and production deployment.

### Workflow

1. Install or update dependencies:
   1. `$ npm install`
1. Start development server at `http://localhost:8080/webpack-dev-server/`:
   1. `$ npm run develop`
      - Will watch source trees for changes.
      - Browser will automatically refresh the web application.

## Deployment

### S3

Assuming a bucket called `my-client-browser-bucket`:

1. Complete guide at [_Hosting Static Web Applications on AWS S3_](https://github.com/michaelahlers/michaelahlers-presence/wiki/Resources#hosting-static-web-applications-on-aws-s3).
1. Generate production-ready assets:
   1. `$ npm run deploy`
1. Upload distribution:
   1. `$ aws s3 sync --delete dist/ s3://my-client-browser-bucket/`

## See Also

### [Resources](https://github.com/michaelahlers/michaelahlers-presence/wiki/Resources)

Several guides and libraries used by this project are documented here.
