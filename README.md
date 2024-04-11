# Book-Scrabble - Tile Management System
This Java-based project simulates a tile management system for a Scrabble-like word game, showcasing various software engineering principles and practices.

## Features
**Immutable Tiles: **Demonstrates the use of immutability in class design, ensuring that tile states remain consistent throughout the game.
**Singleton Bag: **Employs the Singleton design pattern for managing the central tile repository, ensuring global access while preventing multiple instantiations.
**Random and Specific Tile Retrieval:** Illustrates the use of algorithms to manage and manipulate data structures effectively.
**Tile Return Mechanism:** Implements functionality to return tiles to the bag, showcasing the practical use of data structure manipulation.
Software Engineering Experience

## Software engineering Elements & Best Practices:

**Design and Architecture Templates: **Utilizes design patterns like Singleton, demonstrating effective software architecture planning.
**Communication and Server-Client Architecture: **While the current scope focuses on tile management, the structure allows for extension into a server-client model for multiplayer gaming.
**Data Structures / Databases:** Showcases the use of arrays and data manipulation, with potential scalability to utilize databases for persistent storage.
**Data Streaming: **Sets the groundwork for implementing file-based data streaming, essential for game state saving and loading.
**Algorithm Implementation:** Employs algorithms for random tile drawing and specific tile retrieval, essential for game logic.
**Parallel Programming:** The architecture supports extension into using threads for parallel tasks, crucial for real-time multiplayer games.
**Event-Oriented Programming with GUI:** While the current console-based validation demonstrates core logic, the design allows integration with a GUI, showcasing event-driven programming.
![book scru2](https://github.com/Nadav23AnT/Book-Scrabble/assets/71144691/67efba89-d56a-4be0-a0e9-c976d363efd7)


### Getting Started
Ensure Java is installed and follow these steps to run the project:

#### Clone the repository:
`git clone <repository-url>`

#### Navigate to the project directory:
`cd <project-directory>`

#### Compile the Java files:
`javac Tile.java BagValidator.java`

#### Run the validator to test the Bag class functionality:
`java BagValidator`
