import sys
from fcfs_scheduler import FirstComeFirstServedScheduler
from scan_scheduler import ElevatorScanScheduler
from cscan_scheduler import CircularScanScheduler

class DiskSchedulingSimulation:
    """
    Comprehensive disk scheduling simulation and comparison tool
    """
    @staticmethod
    def validate_input(prompt, min_val, max_val):
        """
        Validate user input with error handling
        
        Args:
            prompt (str): Input prompt message
            min_val (int): Minimum allowed value
            max_val (int): Maximum allowed value
        
        Returns:
            int: Validated user input
        """
        while True:
            try:
                value = int(input(prompt))
                if min_val <= value <= max_val:
                    return value
                print(f"Error: Input must be between {min_val} and {max_val}")
            except ValueError:
                print("Invalid input. Please enter an integer.")
    
    @staticmethod
    def run_simulation():
        """
        Execute disk scheduling simulation with multiple algorithms
        """
        print("=== Disk Scheduling Algorithm Simulator ===")
        
        # Get simulation parameters
        total_cylinders = DiskSchedulingSimulation.validate_input(
            "Enter total number of disk cylinders: ", 
            10, 
            100000
        )
        
        request_count = DiskSchedulingSimulation.validate_input(
            "Enter number of cylinder requests: ", 
            1, 
            total_cylinders
        )
        
        start_position = DiskSchedulingSimulation.validate_input(
            f"Enter initial head position (0-{total_cylinders-1}): ", 
            0, 
            total_cylinders-1
        )
        
        # Initialize schedulers
        schedulers = [
            FirstComeFirstServedScheduler(total_cylinders, request_count, start_position),
            ElevatorScanScheduler(total_cylinders, request_count, start_position),
            CircularScanScheduler(total_cylinders, request_count, start_position)
        ]
        
        # Run and compare algorithms
        results = []
        for scheduler in schedulers:
            report = scheduler.detailed_report()
            results.append(report)
            
            # Print individual algorithm results
            print(f"\n{report['algorithm']} Results:")
            print(f"Total Head Movement: {report['total_movement']} cylinders")
            print(f"Cylinder Requests: {report['requests']}")
        
        # Compare and rank algorithms
        print("\n=== Algorithm Performance Comparison ===")
        sorted_results = sorted(results, key=lambda x: x['total_movement'])
        
        for rank, result in enumerate(sorted_results, 1):
            print(f"{rank}. {result['algorithm']}: {result['total_movement']} cylinders")

def main():
    """
    Entry point for the disk scheduling simulation
    """
    try:
        DiskSchedulingSimulation.run_simulation()
    except KeyboardInterrupt:
        print("\n\nSimulation terminated by user.")
    except Exception as e:
        print(f"An unexpected error occurred: {e}")

if __name__ == "__main__":
    main()
