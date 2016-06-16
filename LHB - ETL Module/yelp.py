# -*- coding: utf-8 -*-
import argparse
import rauth
import pandas as pd
import ast
import csv
import json
import pprint
import sys
import urllib
import urllib2
import time
import oauth2
import collections
from itertools import izip

def flatten(d, parent_key='', sep='_'):
    items = []
    for k, v in d.items():
        new_key = parent_key + sep + k if parent_key else k
        if isinstance(v, collections.MutableMapping):
            items.extend(flatten(v, new_key, sep=sep).items())
        else:
            items.append((new_key, v))
    return dict(items)

def get_search_parameters(location, offset):
  #See the Yelp API for more details
  params = {}
  params["term"] = "restaurant"
  params["location"] = str(location)
  params["limit"] = "20"
  params["offset"] = str(offset)
  return params
# OAuth credential placeholders that must be filled in by users.

def get_results(params):

  CONSUMER_KEY = ''
  CONSUMER_SECRET = ''
  TOKEN = ''
  TOKEN_SECRET = ''

  session = rauth.OAuth1Session(
    consumer_key = CONSUMER_KEY
    ,consumer_secret = CONSUMER_SECRET
    ,access_token = TOKEN
    ,access_token_secret = TOKEN_SECRET)

  request = session.get("http://api.yelp.com/v2/search",params=params)

  #Transforms the JSON API response into a Python dictionary
  data = request.json()
  if not data:
    return None
  for k in data:
    if 'error' in k:
      return None
  session.close()

  return data

def main():
  locations = [94102 ,94103, 94104, 94105, 94107, 94108, 94109, 94110, 94111, 94112, 94114, 94115, 94116, 94117, 94118, 94121, 94122, 94123, 94124, 94127, 94129, 94130, 94131, 94132, 94133, 94134, 94158]

  api_calls = []
  for location in locations:
    for i in range(0, 1000, 20):
      params = get_search_parameters(location, offset=i)
      response = get_results(params)
      if not response:
        continue
      api_calls.append(response)
      # with open('yelp_raw.txt', 'w') as file:
      #   json.dumps(response, file)
      # with open("yelp_raw.txt", "r") as file:
      #   yelps = json.load(file)
      #   for yelp in yelps:
      #       yelp_data.append(yelp)
      #Be a good internet citizen and rate-limit yourself

  yelp_data = []
  for i in api_calls:
       yelp_data.append(i['businesses'])

  yelp_data = [item for sublist in yelp_data for item in sublist]
  yelps = pd.DataFrame()
  yelps['id'] = map(lambda yelp: (yelp['id']), yelp_data)
  yelps['name'] = map(lambda yelp:(yelp['name']), yelp_data)
  yelps['is_closed'] = map(lambda yelp:(yelp['is_closed']), yelp_data)
  yelps['image_url'] = map(lambda yelp:(yelp.get('image_url')), yelp_data)
  yelps['url'] = map(lambda yelp:(yelp['url']), yelp_data)
  yelps['mobile_url'] = map(lambda yelp:(yelp['mobile_url']), yelp_data)
  yelps['phone'] = map(lambda yelp:(yelp.get('phone')),yelp_data)
  yelps['display_phone'] = map(lambda yelp:(str(yelp.get('display_phone')))[1:],yelp_data)
  yelps['address'] = map(lambda yelp:(str(yelp['location']['address'])[3:-2]), yelp_data)
  yelps['display_address'] = map(lambda yelp:(yelp['location']['display_address'][0]), yelp_data)
  yelps['latitude'] = (map(lambda yelp: ((yelp[u'location'].get(u'coordinate').get(u'latitude')) if(yelp[u'location'].get(u'coordinate')) else 0), yelp_data))
  yelps['longitude'] = (map(lambda yelp: ((yelp[u'location'].get(u'coordinate').get(u'longitude')) if(yelp[u'location'].get(u'coordinate')) else 0), yelp_data))
  yelps['review_count'] = map(lambda yelp:(yelp.get('review_count')), yelp_data)
  yelps['categories'] = map(lambda yelp:(yelp['categories'][0][0] if yelp.get('categories') else None), yelp_data)
  yelps['rating'] = map(lambda yelp:(yelp['rating']), yelp_data)
  yelps['rating_image_url'] = map(lambda yelp:(yelp['rating_img_url']), yelp_data)
  yelps['rating_image_url_small'] = map(lambda yelp:(yelp['rating_img_url_small']), yelp_data)
  yelps['rating_image_url_large'] = map(lambda yelp:(yelp['rating_img_url_large']), yelp_data)
  # yelps['snippet_text'] = map(lambda yelp:((yelp.get('snippet_text'))), yelp_data)
  yelps['snippet_image_url'] = map(lambda yelp:(yelp.get('snippet_image_url')), yelp_data)

  yelps.to_csv('ratings.csv', index=False, encoding='utf-8')
if __name__ == "__main__":
  main()
