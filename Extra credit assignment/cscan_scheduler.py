import random

class CircularScanScheduler:
    """
    Implements the Circular SCAN (C-SCAN) disk scheduling algorithm
    with improved request handling and movement tracking
    """
    def __init__(self, total_cylinders, request_count, start_position):
        """
        Initialize the C-SCAN scheduler with given parameters
        
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
        Calculate total head movement using C-SCAN algorithm
        
        Returns:
            tuple: (total head movement, list of cylinder requests)
        """
        total_movement = 0
        current_head = self.start_position
        
        # Separate requests into lower and higher segments
        lower_requests = [req for req in self.cylinder_requests if req <= current_head]
        higher_requests = [req for req in self.cylinder_requests if req > current_head]
        
        # Sort in ascending order
        lower_requests.sort()
        higher_requests.sort()
        
        # Track head movements
        head_movements = [current_head]
        
        # Move to the disk's end
        total_movement += abs(self.total_cylinders - 1 - current_head)
        
        # Circular jump back to the beginning
        total_movement += self.total_cylinders - 1
        current_head = 0
        head_movements.append(current_head)
        
        # Service remaining requests
        for request in lower_requests + higher_requests:
            movement = abs(request - current_head)
            total_movement += movement
            current_head = request
            head_movements.append(current_head)
        
        return total_movement, head_movements
    
    def detailed_report(self):
        """
        Generate a detailed report of the C-SCAN scheduling
        
        Returns:
            dict: Comprehensive scheduling information
        """
        movement, path = self.calculate_head_movement()
        
        return {
            "algorithm": "Circular SCAN (C-SCAN) Scheduling",
            "total_movement": movement,
            "head_path": path,
            "requests": self.cylinder_requests
        }
