# ParkMate

ParkMate is a Vehicle Parking Record Manager developed for
Assignment 01 of the Mobile Application Development practical subject.

The application allows a parking attendant to record parking sessions,
calculate charges, and track whether payment has been received.

## Features

- Add parking records.
- View all records and individual record details.
- Update vehicle details, duration, and payment status.
- Delete records after confirmation.
- Preview parking fees while entering data.
- Display the record count and total collected payments.
- Validate required inputs.
- Display an empty-state message when no records exist.

## Activities

| Activity | Purpose |
|---|---|
| MainActivity | Displays parking records and summary totals |
| ParkingFormActivity | Creates and updates records |
| ParkingDetailsActivity | Displays a record and provides Edit/Delete actions |

## Example Parking Rates

| Vehicle type | Rate per hour |
|---|---:|
| Motorcycle | Rs. 50 |
| Car | Rs. 100 |
| Van | Rs. 150 |

These are demonstration rates defined for this project.

Parking duration is entered manually in whole hours from 1 to 12.

Parking fee = hourly rate × parking hours.

The collected total includes only records marked as paid.

## Input–Process–Output

- Input: Vehicle number, vehicle type, parking hours, and payment status.
- Process: Validate inputs, calculate parking fees, and calculate totals.
- Output: Display the fee preview, saved records, details, and summaries.

## Technology and UI

- Java
- XML layouts
- Android Studio
- EditText, RadioGroup, RadioButton, SeekBar, CheckBox, TextView, and Button
- LinearLayout, RelativeLayout, and ScrollView
- Intents for navigation and passing record IDs
- AlertDialog for deletion confirmation

RecyclerView is not used.

## Data Storage

Records are stored temporarily in a shared ArrayList.

Records are not saved to a database or file. They are lost when the
application's process ends.

## Running the Project

1. Clone this repository or download and extract its ZIP.
2. Open the project root folder in Android Studio.
3. Allow Gradle sync to complete.
4. Install Android SDK Platform 37 if requested.
5. Select an emulator or Android device running API 24 or later.
6. Run the app module.

Minimum supported Android version: Android 7.0 (API 24).
Compile SDK and target SDK: 37.