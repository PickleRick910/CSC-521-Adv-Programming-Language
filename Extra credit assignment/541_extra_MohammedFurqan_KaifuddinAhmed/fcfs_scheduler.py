import random

class FirstComeFirstServedScheduler:
    """
    Implements the First-Come, First-Served (FCFS) disk scheduling algorithm
    with improved randomization and tracking
    """
    def __init__(self, total_cylinders, request_count, start_position):
        """
        Initialize the FCFS scheduler with given parameters
        
        Args:
            total_cylinders (int): Total number of disk cylinders
            request_count (int): Number of cylinder requests to generate
            start_position (int): Initial disk head position
        """
        # Use a custom random seed for reproducibility if needed
        random.seed(random.randint(1, 1000))
        
        self.total_cylinders = total_cylinders
        self.request_count = request_count
        self.start_position = start_position
        
        # Generate unique random cylinder requests
        self.cylinder_requests = sorted(random.sample(range(total_cylinders), request_count))
    
    def calculate_head_movement(self):
        """
        Calculate total head movement using FCFS algorithm
        
        Returns:
            tuple: (total head movement, list of cylinder requests)
        """
        total_movement = 0
        current_head = self.start_position
        
        # Track head movements between requests
        head_movements = [current_head]
        
        for request in self.cylinder_requests:
            # Calculate absolute distance between current and next request
            movement = abs(request - current_head)
            total_movement += movement
            
            # Update current head position
            current_head = request
            head_movements.append(current_head)
        
        return total_movement, head_movements
    
    def detailed_report(self):
        """
        Generate a detailed report of the FCFS scheduling
        
        Returns:
            dict: Comprehensive scheduling information
        """
        movement, path = self.calculate_head_movement()
        
        return {
            "algorithm": "First-Come, First-Served (FCFS)",
            "total_movement": movement,
            "head_path": path,
            "requests": self.cylinder_requests
        }
