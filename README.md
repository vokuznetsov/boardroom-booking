# boardroom-booking

Examples of requests:
1) **/booking (POST)**

```
{
  "working-hours": {
    "start": "0900",
    "end": "1730"
  },
  "booking-information": {
    "submission-time": "2011-03-17 10:17:06",
    "employeeId": "EMP001",
    "meeting-time": "2011-03-21 09:00",
    "duration": 2
  }
}
```

2) **/booking?date=2011-03-21 (GET)**

