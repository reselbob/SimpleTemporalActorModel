# temporal-signals
This project demonstrates how to use signals in a Temporal workflow


To start the Temporal Server, execute the following command:

```bash
temporal server start-dev
```

To start the workflow, in a **separate terminal window**, first build the code

```bash
mvn clean package install  
```

Then start the workflow

```bash
mvn exec:java -Dexec.mainClass="temporal.App"
```

