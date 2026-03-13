
  Assuming the driver field is the code I used to get these value, it's the file called tester.java
  
   | Hash Function | Bits | Possible Values | Expected Attempts | Actual Attempts | Time (ms) | Found? |
   |---------------|------|-----------------|-------------------|-----------------|-----------|--------|
   | 2             | 8    | 256             |    256        |    88       |   74       |     yes   |
   | 3             | 16   | 65,536          |   2^16         |    24298    |   250      |   yes     |
   | 1             | 256  | 2^256           |  2^256        |    5,000,000 |  10032     |    no    |

Notes:
*was randomizing both values and comparing rather than having a set of all hashes generated
*timed out after 5 million attempts
*values are the median of three runs


| Hash Function | Bits | Possible Values | Expected Attempts | Actual Attempts | Time (ms) | Found? |
|---------------|------|-----------------|-------------------|-----------------|-----------|--------|
| 2             | 8    | 256             |       256         |       343    |    89       |     yes   |
| 3             | 16   | 65,536          |        65536      |       16463     |   191        |   yes     |
| 1             | 256  | 2^256           |         2^256        |       5,000,000    |      6836     |   no     |
function 2 vals (simple printing byte by byte for each message):
29-40-16-8210439-37-100742112-197854-6346-7-8883-62-899189766-47-117-544566-96-124
38895817-87-44-485771241042612566-411071201236-99-29-105-906424-29-7-1680-18-108-122

function 3 vals:
57-306647-10688-99-11221-25-112-1990-31-115-77-77-96-112116-969-81-88-65-1-30-1-264222
-845031-7511-70-111-61115-89-915587-86173-411227-9016-703059114-81-8692-60-1686-94

Function 1 vals: n/a

*also using median

| Difficulty (d) | Target Set Size \|Y\| | Expected Attempts       | Actual Attempts | Time (ms) | Found? |
|----------------|---------------------|------------------------|-----------------|-----------|--------|
| 12             | 2^244               |          2^12              |     1365     |   42        |   y     |
| 16             | 2^240               |          2^16              |      38174     |     109     |   y     |
| 20             | 2^236               |           2^20             |      619858    |     434     |    y    |
| 24             | 2^232               |          2^24             |     8454206     |    3578  |     y   |

*this one timed out after 10 million iterations
*still using median