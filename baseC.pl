base([
event('EGF',bind,'EGFR'),
event('MEK',prevent,'Ras'),
event('Ras',activate,'CREB'),
event('CREB',inhibit,'Ras')
]).
