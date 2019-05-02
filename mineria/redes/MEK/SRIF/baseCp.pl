base([
event('EGF',bind,'EGFR'),
event('EGFR',activate,'Ras'),
event('Ras',activate,'CREB'),
event('CREB',bind,'SRIF')
]).
