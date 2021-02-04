## Morpheus AWS Billing Export Task Plugin

This plugin provides a Task Type for exporting Daily or Hourly Billing Report Files (in CSV.GZ format) From one Source S3 Bucket into another Target S3 Bucket with filtering of accounts.

Some MSPs may want to provide sub tenant usage reports for Morpheus customers to ingest like they normally would from AWS Daily Reports. This allows a mirror of the files to be made that can be scoped down to a single or a list of specific AWS Usage Account Ids.

### Building

Building the project is simple when using Gradle. Checkout the repository and from the root of the project simply run

```bash
./gradlew morpheus-aws-billing-export-plugin:shadowJar
```

This will produce a `.jar` file in the `./plugins` directory of the project. 

### Installing and Using

Using Morpheus version `5.2.3` or Higher, simply upload the jar file to the Plugin Management page in `Administration -> Integrations -> Plugins`.

One the plugin is uploaded a new Task Type becomes available called `AWS Billing Report Export`. Simply create a new Task and fill out the necessary information.

Set the credentials necessary to read the S3 Bucket, the folder the report resides in, and the report name prefix for the report. Finally set the target bucket as well as the list of usage accounts to filter out.

The billing period input is **optional** . If not specified, the current billing period will be assumed based on the run date.

Once the task is saved, one can instantly execute the task to test it and check the execution information in the `Executions` tab. For using with a customer, simply create a recurring job that runs the task daily.

### Things to Be Done

* Add Support for global Percentage Markup
* Make some inputs editable during particular run


### Contributing

Feel free to contribute to the project and submit PRs. Use Github issues for reporting any issues.

