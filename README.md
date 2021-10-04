# Plutus - Now on the Google Play Store!
Utilities to help calculate pensions!

## Description
This is an app that provides utilities to help pensions administrators carry out their role! You can calculate pension commencement lump sums, calculate the duration between two dates giving you multiple units of time, and calculate the revaluation rates between two dates as well!  You can also search through inflation data, using information obtained for the Office of National Statistics (ONS) API, which will keep up to date.  It is also on the Google Play Store, where you should search Plutus Carkzis, as Plutus is apparently a common name.

## Dependencies
* Android Studio. See also [app level](https://github.com/Carkzis/Plutus/blob/master/app/build.gradle) and [project level](https://github.com/Carkzis/Plutus/blob/master/build.gradle) gradle builds.
* Android SDK 26 for running the app.

## Installing
* You can install it from the Google Play Store! Search Plutus Carkzis, it is a yellow dollar sign on a blue border.  Yes, the app relates to UK pensions, so it should be a pound?  New logo is in the pipeline...
* You can also see it on https://play.google.com/store/apps/details?id=com.carkzis.android.plutus
* Alternatively:
  * You can download the code from the Arkyris repository by clicking "Code", then "Download ZIP".
  * You can then install this from within Android Studio onto an emulator or a mobile device with a minimum SDK of 26 via the "Run 'app'" command (Shift+F10 by default).

## Executing the program
* You can run the app off a suitable emulator/mobile device.
* Main Screen: You can choose to go to the various calculators from here, or the inflation information or about screen.

<img src="https://github.com/Carkzis/Plutus/blob/master/plutus_screenshots/plutus_main.jpg?raw=true" width="300" />

* PCLS Calculator: This is for calculating pension commencement lump sums.  You input the annual pension (sorry, can't calculate that in the app as it can get quite complex!), add the commutation factor (the app doesn't accept multiple commutation factors currently), add the defined contribution fund and select the appropriate standard lifetime allowance.  Click calculate and there you go!  A list of the available options.
  * DISCLAIMER: While every effort has been taken to ensure the calculation is correct and the time of writing, please only consider the results as a rough guide.  Pensions legislation, and scheme rules, can get very complex and often change. Always check the scheme rules, and get your calculations double checked by someone else!

<img src="https://github.com/Carkzis/Plutus/blob/master/plutus_screenshots/plutus_pcls_lta.jpg" width="300" />

* Date Calculator:  This will calculate the duration between two dates, using different units of time.  Click the text entry boxes, and it will bring up a calendar dialogue for you to choose the dates with.  Note that these calculations take into account the last day, so 06/04/2020 to 05/04/2021 counts as one full year!

<img src="https://github.com/Carkzis/Plutus/blob/master/plutus_screenshots/plutus_date.jpg?raw=true" width="300" />

* Revaluation Calculator: This will calculation the revaluation rate between two dates.  Enter the start and end dates, and the revaluation rates for the Consumer Price Index (CPI), Retail Price Index (RPI) and Fixed Rate Guaranteed Minimum Pension (GMP) over the period will be calculated. Note:
  * If this is your first time using the calculator, the app will attempt to download the information from the ONS API, you can then attempt the calculation again.  This is because the calculation requires the inflation data to be held in memory.
  * The calculator will not calculate further ahead than the figures held in memory allows.  For example, in order to calculate the revaluation rate for any period in 2022, you need to have the September 2021 CPI and RPI rates.
  * If the start date is prior to 6 April 1978, the start date for the GMP calculations will be treated as 6 April 1978, and this was when GMP was introduced.
  * If the start date is prior to 1 January 1986, the CPI and RPI rates will be set to 1.0 as revaluation rates are not applied in these instances.
  * If the start date is prior to 1 January 2009, the CPI and RPI max 2.5% rates will consider the start date to be 1 January 2009.

<img src="https://github.com/Carkzis/Plutus/blob/master/plutus_screenshots/plutus_reval.jpg?raw=true" width="300" />

* Inflation Information: There is various information here, with all the CPI, RPI and their 12-month percentages obtained from the ONS API.  You can filter the data by date and year using the search bar.  The links for the information are as follows:
  * CPI: https://www.ons.gov.uk/economy/inflationandpriceindices/timeseries/d7bt/mm23/data
  * RPI: https://www.ons.gov.uk/economy/inflationandpriceindices/timeseries/d7g7/mm23/data
  * CPI 12-Month Percentage: https://www.ons.gov.uk/economy/inflationandpriceindices/timeseries/czbh/mm23/data
  * RPI 12-Month Percentage: https://www.ons.gov.uk/economy/inflationandpriceindices/timeseries/czbh/mm23/data

<img src="https://github.com/Carkzis/Plutus/blob/master/plutus_screenshots/plutus_inflation.jpg?raw=true" width="300" />
<img src="https://github.com/Carkzis/Plutus/blob/master/plutus_screenshots/plutus_cpi.jpg?raw=true" width="300" />
<img src="https://github.com/Carkzis/Plutus/blob/master/plutus_screenshots/plutus_rpi_sorted.jpg?raw=true" width="300" />
<img src="https://github.com/Carkzis/Plutus/blob/master/plutus_screenshots/plutus_gmp.jpg?raw=true" width="300" /> 

## Authors
Marc Jowett (carkzis.apps@gmail.com)

## Version History
* 0.1
  * Initial Release. See on [Google Play Store](https://play.google.com/store/apps/details?id=com.carkzis.android.plutus) or in [commits](https://github.com/Carkzis/Plutus/commits/master).

## License
This is licensed under the BSD-3-Clause License.  You can see the LICENSE.md for further details.

## Acknowledgements
* [The Android Open Source Project](https://source.android.com/) for the fantastic amount of information to help coders in an accessible way.
* [Jose Alcerreca](https://gist.github.com/JoseAlcerreca/5b661f1800e1e654f07cc54fe87441af) for the Event class, no more reappearing toasts or unwanted navigations!
* [Office for National Statistics](https://www.ons.gov.uk/) for having just so much information and making it accessible to everyone.
