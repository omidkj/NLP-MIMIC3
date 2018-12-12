# 619 Final Project

Please create a branch of `master` called `dev` and submit a pull request back to `master`


## Project pipeline:

#### 1) Data_prep_pre_NLP.ibynb :
Here I read the dataset from MIMIC-III database and prepare it to run in my NLP pipeline. This process includes:
* Keeping the last Discharge note for each patient
* Choose a sample of the dataset to be processed in NLP model with the ratio of 0.2
* Cleaning texts from newlines and tabs to make it ready to be processed in NLP model

#### 2) NLP pipeline in Scala: omidkj-final-project/src/main/scala/Main.scala --> object TestAECSV
This pipeline reads a CSV file process it WITH CTAKES and a manual engine to capture to resolve Cui's, Negation, Uncertainty, and SignSymptoms.
Then save the results as into two separate CSV file: 
* classification_csv.csv
* riskfactors_csv.csv

#### 3) Data_prep_post_NLP.ibynb :
In this phase, I read the data saved in the NLP process and prepare it for the ML models:
* Extracting three features "cui_exist, negation_exist, and uncertainty_exist" out of 'class' column that was saved during NLP pipeline
* Labeling the data based on ICD codes matched with Pulmonary Embolism
* Saivng the final dataset for the ML models,  file named "Final_model_mix" 

#### 4) Final_model_mix.ibynb :
In this file we run three different ML models (Deep Neural network, Naive Bayes Classifier, and Random Forest Classifier in three different senarios (All the features, without Negation and Uncertainty, and without Uncertainty) in total nine models.
