board = [[0]*8 for i in range(8)]

print("Blank board: \n")
print(board)
print("\n \n")

def checklegal(column, row):

    for i in range(0, 8):
        if(board[i][row] == 1):
            return False

    # diagonal L-R check

    # first find the corners
    new_row = row
    new_col = column
    while(new_row != 0 and new_col != 0):
            new_row = new_row - 1
            new_col = new_col - 1
    # now that corners are found, iterate down through the board until either one hits 8 or a 1 is found
    while(new_row != 8 and new_col != 8):
        if(board[new_col][new_row] == 1):
            return False
        new_row = new_row + 1
        new_col = new_col + 1

    # diagonal R-L check
    # first find the corners
    new_row = row
    new_col = column
    while(new_row != 7 and new_col != 0):
        new_row = new_row + 1
        new_col = new_col - 1
    # now that corners are found, iterate down through the board until either one hits 0
    while(new_row != -1 and new_col != 8):
        if(board[new_col][new_row] == 1):
            return False
        new_row = new_row - 1
        new_col = new_col + 1

    # if none of the above conditions are met, it's safe to place the queen
    return True

# function to test if entering on a fail or success
def isfail(column):
    for i in range(0, 8):
        # for failure: if there's already a queen there, return location of queen
        if(board[column][i] == 1):
            return i
    return -1
                    

#function to place queen recursively

def placequeen(column):
    print(column)
    if(column == 8):
        print("Final board: \n")
        print(board)
    else:
        # if it's not a fail, check every column
        if(isfail(column) == -1):
            for i in range(0, 8):
                if(checklegal(column, i) == True):
                    board[column][i] = 1
                    placequeen(column + 1)
                    return
            placequeen(column - 1)
        # if it is a fail, only check from where you are
        else:
            counter = isfail(column)
            # clear old queen
            board[column][counter] = 0
            for i in range(counter + 1, 8):
                if(checklegal(column, i) == True):
                    board[column][i] = 1
                    placequeen(column + 1)
                    return
            placequeen(column - 1)
            

# enter the "main"
column = 0
placequeen(0)
