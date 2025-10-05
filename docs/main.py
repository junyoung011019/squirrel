from http.server import SimpleHTTPRequestHandler, HTTPServer
import mimetypes

mimetypes.add_type('application/yaml', '.yaml')
mimetypes.add_type('application/yaml', '.yml')

class CustomHandler(SimpleHTTPRequestHandler):
    pass

if __name__ == '__main__':
    port = 8080
    print(f"Serving on http://localhost:{port}")
    HTTPServer(('0.0.0.0', port), CustomHandler).serve_forever()
