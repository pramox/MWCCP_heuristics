# Heuristic Algorithms for the Minimum Weighted Crossing with Constraints Problem (MWCCP)

## Overview

This repository contains the implementation of various heuristic optimization techniques for solving the **Minimum Weighted Crossing with Constraints Problem (MWCCP)**. The project is part of the **Heuristic Optimization Techniques** course at **TU Wien** during the Winter Semester 2024.

The goal of this assignment is to explore and develop heuristic algorithms including construction heuristics, local search frameworks, and advanced metaheuristics, all while applying concepts such as delta evaluation and neighborhood structure design.

## Problem Statement

The MWCCP involves finding an optimal node ordering in a graph-like structure to minimize weighted edge crossings, subject to specific constraints. This problem has practical relevance in areas such as:

- VLSI design
- Metro/railway map layout
- Software engineering class diagrams
- Storyline visualization in narrative design

## Implemented Features

### Construction Heuristics
- **Deterministic Construction Heuristic**  
  Builds a solution greedily based on edge weights and constraint satisfaction.
- **Randomized Construction Heuristic**  
  Introduces controlled randomness to generate diverse initial solutions.

### Local Search Framework
- Supports **three neighborhood structures**
- Implements **step functions**:
  - First improvement
  - Best improvement
  - Random selection

### Metaheuristics
- **Variable Neighborhood Descent (VND)**
- **Greedy Randomized Adaptive Search Procedure (GRASP)**
- **Simulated Annealing (SA)** (or optionally GVNS/Tabu Search)

### Delta Evaluation
- Incremental computation of objective changes for faster evaluation.
- Asymptotic runtime comparison and preprocessing applied where beneficial.

## Experimental Setup

### Parameter Tuning
- Parameters such as:
  - Randomness degree
  - Neighborhood size
  - Cooling schedule (SA)
  - Tabu list size (TS)
- Manual tuning to study impact on performance and solution quality.


