package Project;

public abstract class ParkingBlocks {
    private String blockName;
    private int totalSpaces;
    private boolean[][] spaceAvailability;

    public ParkingBlocks(String blockName, int totalSpaces) {
        this.blockName = blockName;
        this.totalSpaces = totalSpaces;
        this.spaceAvailability = new boolean[5][totalSpaces];
        // Initialize space availability, e.g., all spaces are initially available
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < totalSpaces; j++) {
                spaceAvailability[i][j] = true;
            }
        }
    }

    public String getBlockName() {
        return blockName;
    }

    public int getTotalSpaces() {
        return totalSpaces;
    }

    public boolean[][] getSpaceAvailability() {
        return spaceAvailability;
    }

    public void setSpaceAvailability(int floor, int spaceNumber, boolean availability) {
        spaceAvailability[floor - 1][spaceNumber - 1] = availability;
    }

    public abstract void reserveSpace(String studentId, int floor, int spaceNumber);

    public abstract void displaySpaces();

    public abstract void displayReservationSummary(String studentId);

    // Additional method to reset all space availabilities
    public void resetSpaces() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < getTotalSpaces(); j++) {
                spaceAvailability[i][j] = true;
            }
        }
    }

    // Additional method to check if all spaces are reserved
    public boolean allSpacesReserved() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < getTotalSpaces(); j++) {
                if (spaceAvailability[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}

