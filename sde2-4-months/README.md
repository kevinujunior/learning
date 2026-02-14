## Project Settings Instructions

To keep the project structure clean, all compiled `.class` files are generated inside a dedicated `out` directory.

---

### Java Configuration for Code Runner (VS Code)

#### Prerequisites
- Install **Code Runner** extension by **Jun Han** in Visual Studio Code.

---

#### Configuration Steps

1. Open **VS Code**
2. Navigate to:  
   **File → Settings**
3. Search for:  
   `code-runner.executorMap`
4. Click **Edit in settings.json**
5. Add or update the following entry:

```json
"code-runner.executorMap": {
  "java": "cd $dir && javac -d $workspaceRoot/out/compiled_classes $fileName && java -cp $workspaceRoot/out/compiled_classes $fileNameWithoutExt"
}
```