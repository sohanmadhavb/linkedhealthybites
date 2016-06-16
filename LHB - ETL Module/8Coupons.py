__author__ = 'Shashank'
import requests
import json
import os
import pandas as pd
zips = [94102, 94103, 94104, 94105, 94107, 94108, 94109, 94110, 94111, 94112, 94114, 94115, 94116, 94117, 94118, 94121, 94122, 94123, 94124, 94127, 94129, 94130, 94131, 94132, 94133, 94134, 94158]
coupon_data = []
for i in zips:
    url = 'http://api.8coupons.com/v1/getdeals?key=&zip='+str(i)+'&mileradius=20&limit=1000&orderby=radius&categoryid=1'
    payload = { 'key' : 'val' }
    headers = {}
    res = requests.post(url, data=payload, headers=headers)
    with open('raw_deals.txt', 'w') as file:
        file.write(res.text)
    with open("raw_deals.txt", "r") as file:
        coups = json.load(file)
        for coup in coups:
            coupon_data.append(coup)
print coupon_data
coupons = pd.DataFrame()
coupons['id'] = map(lambda coupon: (coupon['ID']), coupon_data)
coupons['url'] = map(lambda coupon: (coupon['URL']), coupon_data)
coupons['name'] = map(lambda coupon: (coupon['name']), coupon_data)
coupons['phone'] = map(lambda coupon: (coupon['phone']), coupon_data)
coupons['homepage'] = map(lambda coupon:(coupon['homepage']), coupon_data)
coupons['address'] = map(lambda coupon: (coupon['address']), coupon_data)
coupons['city'] = map(lambda coupon:(coupon['city']), coupon_data)
coupons['state'] = map(lambda coupon:(coupon['state']), coupon_data)
coupons['lat'] = map(lambda coupon:(coupon['lat']), coupon_data)
coupons['lon'] = map(lambda coupon:(coupon['lon']), coupon_data)
coupons['expiry'] = map(lambda coupon:(coupon['expirationDate']), coupon_data)
coupons['postdate'] = map(lambda coupon:(coupon['postDate']), coupon_data)
coupons['provider'] = map(lambda coupon:(coupon['providerName']), coupon_data)
coupons['down'] = map(lambda coupon:(coupon['down']), coupon_data)
coupons['up'] = map(lambda coupon:(coupon['up']), coupon_data)
coupons['dealtitle'] = map(lambda coupon: (coupon['dealTitle']), coupon_data)
coupons['dealinfo'] = map(lambda coupon: (coupon['dealinfo']), coupon_data)
coupons['dealoriginalprice'] = map(lambda coupon: (coupon['dealOriginalPrice']), coupon_data)
coupons['dealprice'] = map(lambda coupon: (coupon['dealPrice']), coupon_data)
coupons['dealdiscount'] = map(lambda coupon: (coupon['dealDiscountPercent']), coupon_data)
coupons['dealsavings'] = map(lambda coupon: (coupon['dealSavings']), coupon_data)
coupons['disclaimer'] = map(lambda coupon: (coupon['disclaimer']), coupon_data)
coupons['dealsinstore'] = map(lambda coupon: (coupon['totalDealsInThisStore']), coupon_data)
coupons['storeurl'] = map(lambda coupon: (coupon['storeURL']), coupon_data)
coupons['showimage'] = map(lambda coupon: (coupon['showImage']), coupon_data)
coupons['showimagestandardbig'] = map(lambda coupon: (coupon['showImageStandardBig']), coupon_data)
coupons['showimagestandardsmall'] = map(lambda coupon: (coupon['showImageStandardSmall']), coupon_data)

coupons.to_csv('Deals.csv', index=False)
